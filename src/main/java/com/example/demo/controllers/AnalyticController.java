package com.example.demo.controllers;

import com.example.demo.dto.response.TotalSellingResponse;
import com.example.demo.services.analytic.AnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AnalyticController {

    private final AnalyticService analyticService;

    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/total")
    ResponseEntity<TotalSellingResponse> getTotalSells(){
        return this.analyticService.getTotalSells();
    }
}
