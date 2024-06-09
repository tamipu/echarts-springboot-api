package com.example.echarts.model;


import lombok.Getter;

@Getter
public enum ChartType {
    PIE("pie-chart"),
    RADAR("radar-chart"),
    STACKED_BAR("stacked-bar-100"),
    RADAR_CHART("radar-chart"),
    LINE_AREA("line-area"),
    SEMI_CIRCLE_GAUCHE("semi-circle-gauche"),
    STACKED_COLUMN("stacked-column.json"),
    BUBBLE_CHART("bubble-chart"),
    SPARKLINE("sparkline.json"),
    SPARKLINENG("sparklineNG"),
    SCATTER("scatter"),
    BOXPLOT("boxplot");

    private final String chartName;

    ChartType(String chartName) {
        this.chartName = chartName;
    }

    public static ChartType fromString(String chartName) {
        for (ChartType chartType : ChartType.values()) {
            if (chartType.chartName.equals(chartName)) {
                return chartType;
            }
        }
        throw new IllegalArgumentException("Unknown chart name: " + chartName);
    }

}

