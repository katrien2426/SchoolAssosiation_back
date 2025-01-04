package com.katrien.controller;

import com.katrien.common.R;
import com.katrien.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public R<Map<String, Integer>> getStatistics() {
        Map<String, Integer> statistics = statisticsService.getStatistics();
        return R.success(statistics);
    }
}