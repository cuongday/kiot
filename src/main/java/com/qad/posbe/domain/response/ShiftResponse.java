package com.qad.posbe.domain.response;

import com.qad.posbe.util.constant.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponse {
    private Long id;
    private UserInfoResponse user;
    private Instant startTime;
    private Instant endTime;
    private Long startCash;
    private Long endCash;
    private Long expectedCash;
    private Long cashDifference; // Chênh lệch giữa tiền thực tế và dự kiến
    private ShiftStatus status;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
    private Long cashRevenue; // Doanh thu tiền mặt
    private Long transferRevenue; // Doanh thu chuyển khoản
    private Long totalRevenue; // Tổng doanh thu
    private Integer orderCount; // Số lượng đơn hàng
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfoResponse {
        private Long id;
        private String name;
        private String username;
    }
} 