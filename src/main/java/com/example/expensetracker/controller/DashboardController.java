package com.example.expensetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.expensetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.currentMonth());
        return "dashboard";
    }
}
