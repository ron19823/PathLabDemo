package com.lab.demo.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
public class LoginController {

    @GetMapping({"/login", "/loginPage"})
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            Model model,
            jakarta.servlet.http.HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        log.info("GET /login called. User-Agent: {}", userAgent);

        if ("device".equals(error)) {
            log.warn("Login attempt from disallowed device.");
            model.addAttribute("errorMsg", "Login from this device is not allowed.");
        } else if (error != null) {
            log.warn("Login attempt with invalid credentials.");
            model.addAttribute("errorMsg", "Invalid username or password.");
        }
        model.addAttribute("deviceType", getDeviceType(userAgent));
        return "loginPage";
    }

    @PostMapping("/login-check")
    public String login(jakarta.servlet.http.HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        log.info("POST /login-check called. User-Agent: {}", userAgent);

        if (!isDesktop(userAgent)) {
            log.warn("Non-desktop device tried to login. Redirecting to /error=device");
            return "redirect:/error=device";
        }
        log.info("Desktop device login successful. Redirecting to /dashboard");
        return "redirect:/dashboard";
    }

    private boolean isDesktop(String userAgent) {
        if (userAgent == null) return false;
        String ua = userAgent.toLowerCase();
        return !(ua.contains("android") || ua.contains("iphone") || ua.contains("ipad") || ua.contains("mobile") || ua.contains("tablet"));
    }

    private String getDeviceType(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("android") || ua.contains("iphone") || ua.contains("ipad") || ua.contains("mobile") || ua.contains("tablet")) {
            return "Mobile/Tablet";
        }
        return "Desktop";
    }
}
