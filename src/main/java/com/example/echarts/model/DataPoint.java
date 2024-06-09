package com.example.echarts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataPoint {
    private final int x;
    private final int y;
    private final int z;
}
