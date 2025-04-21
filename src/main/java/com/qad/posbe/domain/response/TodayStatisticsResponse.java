package com.qad.posbe.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayStatisticsResponse {
    private long value;
    private int count;
    private CompareData compareYesterday;
    private CompareData compareLastMonth;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompareData {
        private double percentage;
        private String trend;
    }
} 