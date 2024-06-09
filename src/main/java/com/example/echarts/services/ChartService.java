package com.example.echarts.services;

import com.example.echarts.model.ChartType;
import com.example.echarts.model.DataPoint;
import com.example.echarts.model.Serie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ChartService {

    public String getTemplateByName(ChartType chartType) {
        String templateName = chartType.getChartName();
        if (templateName.endsWith(".json")) {
            templateName = templateName.substring(0, templateName.length() - 5); // remove .json if present
        }
        Resource resource = new ClassPathResource("templates/" + templateName + ".json");
        try {
            Path path = Paths.get(resource.getURI());
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            log.error("Error during reading template {}", templateName, e);
            return "{\"error\": \"Template not found\"}";
        }
    }

    public List<Serie> getSeries(ChartType chartType) {
        return switch (chartType) {
            case STACKED_BAR -> Arrays.asList(
                    new Serie("A", Arrays.asList(30, 40, 35, 50, 49, 60)),
                    new Serie("B", Arrays.asList(20, 30, 45, 60, 70, 91)),
                    new Serie("C", Arrays.asList(20, 29, 37, 36, 44, 45)),
                    new Serie("D", Arrays.asList(25, 20, 15, 25, 32, 42)),
                    new Serie("E", Arrays.asList(15, 10, 5, 15, 22, 32))
            );
            case PIE -> Arrays.asList(
                    new Serie("Team A", List.of(44)),
                    new Serie("Team B", List.of(55)),
                    new Serie("Team C", List.of(13)),
                    new Serie("Team D", List.of(43)),
                    new Serie("Team E", List.of(22))
            );
            case RADAR -> Arrays.asList(
                    new Serie("Series 1", Arrays.asList(70, 10, 80, 40, 100, 20)),
                    new Serie("Series 2", Arrays.asList(80, 30, 50, 10, 10, 70))
            );
            case SEMI_CIRCLE_GAUCHE -> List.of(
                    new Serie("Access From", Arrays.asList(1048, 735, 580, 484, 300))
            );
            case STACKED_COLUMN -> Arrays.asList(
                    new Serie("A", Arrays.asList(32, 40, 35, 50, 49, 60)),
                    new Serie("B", Arrays.asList(20, 30, 45, 60, 70, 91)),
                    new Serie("C", Arrays.asList(20, 29, 37, 36, 44, 45)),
                    new Serie("D", Arrays.asList(25, 20, 15, 25, 32, 42)),
                    new Serie("E", Arrays.asList(15, 10, 5, 15, 22, 32))
            );
            case LINE_AREA -> Arrays.asList(
                    new Serie("Good", Arrays.asList(1, 4, 2, 51, 42)),
                    new Serie("Good operator", Arrays.asList(50000, 50000, 50000, 50000, 50000)),
                    new Serie("PPM", Arrays.asList(10000, 20000, 25000, 32000, 38000))
            );
            case SPARKLINE -> {
                List<Integer> data = Arrays.asList(10, 15, 20, 25, 30);
                yield List.of(new Serie("DPMO", data));
            }

            case SPARKLINENG -> {
                List<DataPoint> dataPoints = IntStream.range(0, 0)
                        .mapToObj(i -> new DataPoint((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)))
                        .toList();
                yield List.of(
                        new Serie("Data", dataPoints.stream().map(DataPoint::getX).collect(Collectors.toList()))
                );
            }
            case SCATTER -> {
                List<DataPoint> dataPoints = IntStream.range(0, 12)
                        .mapToObj(i -> new DataPoint((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100)))
                        .toList();
                yield List.of(
                        new Serie("0", dataPoints.stream().map(DataPoint::getX).collect(Collectors.toList())),
                        new Serie("1", dataPoints.stream().map(DataPoint::getY).collect(Collectors.toList()))
                );
            }
            case BOXPLOT -> Arrays.asList(
                    new Serie("box", Arrays.asList(54, 66, 69, 75, 88)),
                    new Serie("box", Arrays.asList(43, 65, 69, 76, 81)),
                    new Serie("box", Arrays.asList(31, 39, 45, 51, 59)),
                    new Serie("box", Arrays.asList(39, 46, 55, 65, 71)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("box", Arrays.asList(29, 31, 35, 39, 44)),
                    new Serie("outliers", List.of(69)),
                    new Serie("outliers", List.of(69)),
                    new Serie("outliers", List.of(45)),
                    new Serie("outliers", List.of(55)),
                    new Serie("outliers", List.of(35)),
                    new Serie("outliers", List.of(35)),
                    new Serie("outliers", List.of(35)),
                    new Serie("outliers", List.of(35)),
                    new Serie("outliers", List.of(35)),
                    new Serie("outliers", List.of(35))
            );

            default -> {
                log.error("Unknown chart type: {}", chartType.name());
                yield Collections.emptyList();
            }
        };
    }
}


