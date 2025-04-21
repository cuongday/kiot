package com.qad.posbe.controller;

import com.qad.posbe.domain.response.StatisticsResponse;
import com.qad.posbe.domain.response.TodayStatisticsResponse;
import com.qad.posbe.service.StatisticsService;
import com.qad.posbe.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiMessage("Lấy thống kê doanh thu thành công")
    public ResponseEntity<StatisticsResponse> getRevenueStatistics(
            @RequestParam(name = "month", required = true) int month,
            @RequestParam(name = "year", required = true) int year,
            @RequestParam(name = "type", defaultValue = "daily") String type) {
        
        // Kiểm tra tham số đầu vào
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Tháng phải từ 1 đến 12");
        }
        
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Năm phải từ 2000 đến 2100");
        }
        
        if (!type.equals("daily") && !type.equals("hourly") && !type.equals("weekly")) {
            throw new IllegalArgumentException("Loại thống kê phải là daily, hourly hoặc weekly");
        }
        
        StatisticsResponse response = statisticsService.getRevenueStatistics(month, year, type);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy thống kê doanh thu ngày hôm nay thành công")
    public ResponseEntity<TodayStatisticsResponse> getTodayStatistics() {
        TodayStatisticsResponse response = statisticsService.getTodayStatistics();
        return ResponseEntity.ok(response);
    }
} 