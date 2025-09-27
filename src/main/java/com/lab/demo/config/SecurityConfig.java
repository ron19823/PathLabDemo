package com.lab.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Log4j2
@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        log.info("Initializing InMemoryUserDetailsManager with default user");
        UserDetails user = User.withUsername("user")
                .password("{noop}password") // {noop} = no encoding
                .roles("USER")
                .build();
        log.info("Created user 'user' with role 'USER'");
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain");
        log.info("Adding DeviceCheckFilter before UsernamePasswordAuthenticationFilter");
        
        http
            .addFilterBefore(new DeviceCheckFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> {
                log.info("Configuring authorization rules");
                log.info("Permitting access to /loginPage and /error without authentication");
                log.info("Requiring authentication for all other requests");
                auth.requestMatchers("/loginPage", "/error").permitAll() 
                    .anyRequest().authenticated();
            })
            .formLogin(form -> {
                log.info("Configuring form login");
                log.info("Login page: /loginPage");
                log.info("Login processing URL: /login-check");
                log.info("Default success URL: /dashboard");
                form.loginPage("/loginPage")
                    .loginProcessingUrl("/login-check")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll();
            })
            .logout(logout -> {
                log.info("Configuring logout - permitting all");
                logout.permitAll();
            });
        
        log.info("SecurityFilterChain configuration completed");
        return http.build();
    }

    // Filter to block non-desktop devices and unauthorized locations on login POST
    static class DeviceCheckFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            String servletPath = request.getServletPath();
            String method = request.getMethod();
            
            log.debug("DeviceCheckFilter processing request: {} {}", method, servletPath);
            
            if ("/login-check".equals(servletPath) && "POST".equalsIgnoreCase(method)) {
                String userAgent = request.getHeader("User-Agent");
                String clientIP = request.getRemoteAddr();
                
                log.info("DeviceCheckFilter - Processing login attempt from IP: {}, User Agent: {}", clientIP, userAgent);
                
                // Check device type
                if (!isDesktop(userAgent)) {
                    log.warn("Non-desktop device detected in DeviceCheckFilter. IP: {}, User Agent: {}. Redirecting to /path-lab/loginPage?error=device", clientIP, userAgent);
                    response.sendRedirect("/path-lab/loginPage?error=device");
                    return;
                } else {
                    log.info("Desktop device confirmed. IP: {}", clientIP);
                }
                
                // Check location (latitude and longitude)
                try {
                    String latParam = request.getParameter("latitude");
                    String longParam = request.getParameter("longitude");
                    
                    if (latParam != null && longParam != null) {
                        double latitude = Double.parseDouble(latParam);
                        double longitude = Double.parseDouble(longParam);
                        
                        log.info("DeviceCheckFilter - Location check: Lat: {}, Long: {}, IP: {}", latitude, longitude, clientIP);
                        
                        if (!isWithinAllowedPolygon(latitude, longitude)) {
                            log.warn("Login attempt from disallowed location in DeviceCheckFilter. Lat: {}, Long: {}, IP: {}. Redirecting to /path-lab/loginPage?error=location", latitude, longitude, clientIP);
                            response.sendRedirect("/path-lab/loginPage?error=location");
                            return;
                        } else {
                            log.info("Location check passed. Allowing login attempt to proceed. Lat: {}, Long: {}, IP: {}", latitude, longitude, clientIP);
                        }
                    } else {
                        log.warn("Missing latitude or longitude parameters in login request. IP: {}. Redirecting to /path-lab/loginPage?error=location", clientIP);
                        response.sendRedirect("/path-lab/loginPage?error=location");
                        return;
                    }
                } catch (NumberFormatException e) {
                    log.error("Invalid latitude or longitude format in login request. IP: {}. Error: {}", clientIP, e.getMessage());
                    response.sendRedirect("/path-lab/loginPage?error=location");
                    return;
                }
            }
            
            log.debug("DeviceCheckFilter - Request allowed to proceed: {} {}", method, servletPath);
            filterChain.doFilter(request, response);
        }

        private boolean isDesktop(String userAgent) {
            if (userAgent == null) {
                log.debug("User-Agent is null, treating as non-desktop");
                return false;
            }
            
            String ua = userAgent.toLowerCase();
            boolean isMobile = ua.contains("android") || ua.contains("iphone") || ua.contains("ipad") || ua.contains("mobile") || ua.contains("tablet");
            
            log.debug("Device detection - User Agent: {}, Is Mobile: {}", userAgent, isMobile);
            return !isMobile;
        }
        
        private boolean isWithinAllowedPolygon(double latitude, double longitude) {
            // Define a sample allowed polygon (e.g., a rectangular area for demonstration)
            // In a real scenario, load this from configuration or a database
            double minLat = 27.7128;  // Example: NYC area
            double maxLat = 29.7589;
            double minLong = 74.0060;
            double maxLong = 77.9352;
            
            log.debug("Location validation - Lat: {}, Long: {}, MinLat: {}, MaxLat: {}, MinLong: {}, MaxLong: {}", 
                     latitude, longitude, minLat, maxLat, minLong, maxLong);
            
            boolean isWithinBounds = latitude >= minLat && latitude <= maxLat && longitude >= minLong && longitude <= maxLong;
            log.debug("Location within allowed polygon: {}", isWithinBounds);
            
            return isWithinBounds;
        }
    }
}
