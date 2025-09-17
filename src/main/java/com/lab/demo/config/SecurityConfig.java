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
        UserDetails user = User.withUsername("user")
                .password("{noop}password") // {noop} = no encoding
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new DeviceCheckFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/loginPage", "/error").permitAll() 
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/loginPage")
                .loginProcessingUrl("/login-check")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());
        return http.build();
    }

    // Filter to block non-desktop devices on login POST
    static class DeviceCheckFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            if ("/login-check".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
                String userAgent = request.getHeader("User-Agent");
                log.info("DeviceCheckFilter - User Agent: {}", userAgent);
                if (!isDesktop(userAgent)) {
                    log.warn("Non-desktop device detected in DeviceCheckFilter. Redirecting to /path-lab/loginPage?error=device");
                    response.sendRedirect("/path-lab/loginPage?error=device");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }

        private boolean isDesktop(String userAgent) {
            if (userAgent == null) return false;
            String ua = userAgent.toLowerCase();
            return !(ua.contains("android") || ua.contains("iphone") || ua.contains("ipad") || ua.contains("mobile") || ua.contains("tablet"));
        }
    }
}
