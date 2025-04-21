package com.qad.posbe.controller;

import com.qad.posbe.domain.response.TopMerchandiseResponse;
import com.qad.posbe.service.StatisticsService;
import com.qad.posbe.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchandise")
@RequiredArgsConstructor
@Slf4j
public class MerchandiseController {

    private final StatisticsService statisticsService;

    @GetMapping("/top")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy danh sách sản phẩm bán chạy thành công")
    public ResponseEntity<List<TopMerchandiseResponse>> getTopMerchandise(
            @RequestParam(name = "timeframe", defaultValue = "month") String timeframe,
            @RequestParam(name = "metric", defaultValue = "revenue") String metric,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        
        // Kiểm tra tham số đầu vào
        if (!timeframe.equals("today") && !timeframe.equals("week") && 
            !timeframe.equals("month") && !timeframe.equals("year")) {
            throw new IllegalArgumentException("Khoảng thời gian phải là today, week, month hoặc year");
        }
        
        if (!metric.equals("revenue") && !metric.equals("quantity")) {
            throw new IllegalArgumentException("Loại thống kê phải là revenue hoặc quantity");
        }
        
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Giới hạn số lượng sản phẩm phải từ 1 đến 100");
        }
        
        List<TopMerchandiseResponse> response = statisticsService.getTopMerchandise(timeframe, metric, limit);
        return ResponseEntity.ok(response);
    }
} 