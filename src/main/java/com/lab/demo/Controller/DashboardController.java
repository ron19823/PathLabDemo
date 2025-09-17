package com.lab.demo.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class DashboardController {

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        log.info("GET /dashboard called.");
        return "dashboard";
    }
}