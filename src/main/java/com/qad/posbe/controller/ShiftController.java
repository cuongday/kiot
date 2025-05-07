package com.qad.posbe.controller;

import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.qad.posbe.domain.Shift;
import com.qad.posbe.domain.request.CloseShiftDTO;
import com.qad.posbe.domain.request.OpenShiftDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.domain.response.ShiftResponse;
import com.qad.posbe.service.ShiftService;
import com.qad.posbe.util.annotation.ApiMessage;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
@Slf4j
public class ShiftController {
    
    private final ShiftService shiftService;
    
    /**
     * Mở ca làm việc mới
     */
    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Mở ca làm việc thành công")
    public ResponseEntity<ShiftResponse> openShift(@Valid @RequestBody OpenShiftDTO openShiftDTO) {
        ShiftResponse shift = shiftService.openShift(openShiftDTO);
        return ResponseEntity.ok(shift);
    }
    
    /**
     * Đóng ca làm việc theo ID
     */
    @PostMapping("/close/{id}")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Đóng ca làm việc thành công")
    public ResponseEntity<ShiftResponse> closeShift(
            @PathVariable("id") Long id, 
            @Valid @RequestBody CloseShiftDTO closeShiftDTO) {
        ShiftResponse shift = shiftService.closeShift(id, closeShiftDTO);
        return ResponseEntity.ok(shift);
    }
    
    /**
     * Đóng ca làm việc hiện tại của người dùng đang đăng nhập
     */
    @PostMapping("/close-current")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Đóng ca làm việc hiện tại thành công")
    public ResponseEntity<ShiftResponse> closeCurrentShift(@Valid @RequestBody CloseShiftDTO closeShiftDTO) {
        ShiftResponse shift = shiftService.closeCurrentShift(closeShiftDTO);
        return ResponseEntity.ok(shift);
    }
    
    /**
     * Lấy thông tin ca làm việc hiện tại
     */
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy thông tin ca làm việc hiện tại thành công")
    public ResponseEntity<?> getCurrentShift() {
        try {
            ShiftResponse shift = shiftService.getCurrentShift();
            return ResponseEntity.ok(shift);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("hasOpenShift", false);
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Kiểm tra xem có ca làm việc đang mở không
     */
    @GetMapping("/check-open")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Kiểm tra ca làm việc thành công")
    public ResponseEntity<Map<String, Boolean>> checkOpenShift() {
        boolean hasOpenShift = shiftService.hasOpenShift();
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasOpenShift", hasOpenShift);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lấy thông tin chi tiết ca làm việc
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy thông tin chi tiết ca làm việc thành công")
    public ResponseEntity<ShiftResponse> getShiftById(@PathVariable("id") Long id) {
        ShiftResponse shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(shift);
    }
    
    /**
     * Lấy danh sách ca làm việc có phân trang
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy danh sách ca làm việc thành công")
    public ResponseEntity<ResultPaginationDTO> getAllShifts(
            @Filter Specification<Shift> spec,
            Pageable pageable) {
        ResultPaginationDTO shifts = shiftService.getAllShifts(spec, pageable);
        return ResponseEntity.ok(shifts);
    }
} 