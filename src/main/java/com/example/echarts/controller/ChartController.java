package com.example.echarts.controller;


import com.example.echarts.model.ChartType;
import com.example.echarts.model.Serie;
import com.example.echarts.services.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
public class ChartController {
    @Autowired
    private ChartService chartService;
    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }
    @GetMapping("/templates")
    public String getTemplateByName(@RequestParam("templateName") String templateName) {
        return chartService.getTemplateByName(ChartType.fromString(templateName));
    }
    @GetMapping("/series")
    public List<Serie> getSeries(@RequestParam("chartName") String chartName) {
        return chartService.getSeries(ChartType.fromString(chartName));
    }
}

