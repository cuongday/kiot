package com.qad.posbe.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsResponse {
    private String type;
    private List<StatisticsItemResponse> data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatisticsItemResponse {
        // Các trường có thể là day, hour, hoặc weekday tùy thuộc vào loại thống kê
        private String label;
        private Long value;
    }
} 