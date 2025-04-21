package com.qad.posbe.controller;

import com.qad.posbe.domain.ImportHistory;
import com.qad.posbe.domain.request.ImportHistoryRequest;
import com.qad.posbe.domain.response.ImportExcelResponse;
import com.qad.posbe.domain.response.ImportHistoryResponse;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.repository.SupplierRepository;
import com.qad.posbe.service.ImportExcelService;
import com.qad.posbe.service.ImportService;
import com.qad.posbe.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/imports")
@RequiredArgsConstructor
@Slf4j
public class ImportController {
    private final ImportService importService;
    private final SupplierRepository supplierRepository;
    private final ImportExcelService importExcelService;

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Tạo lịch sử nhập hàng thành công")
    public ResponseEntity<ImportHistoryResponse> createImportHistory(@Valid @RequestBody ImportHistoryRequest request) {
        ImportHistory entity = this.importService.createImportHistory(request);
        ImportHistoryResponse response = this.importService.toImportHistoryResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiMessage("Lấy thông tin lịch sử nhập hàng thành công")
    public ResponseEntity<ImportHistoryResponse> getImportHistoryById(@PathVariable("id") Long id) {
        ImportHistory entity = importService.getImportHistoryById(id);
        ImportHistoryResponse response = importService.toImportHistoryResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('admin')")
    @ApiMessage("Lấy danh sách lịch sử nhập hàng thành công")
    public ResponseEntity<ResultPaginationDTO> getAllImportHistories(
            @Filter Specification<ImportHistory> importSpec,
            Pageable pageable) {
        
        ResultPaginationDTO result = this.importService.getAllImportHistories(importSpec, pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiMessage("Cập nhật lịch sử nhập hàng thành công")
    public ResponseEntity<ImportHistoryResponse> updateImportHistory(
            @PathVariable Long id,
            @Valid @RequestBody ImportHistoryRequest request) {
        ImportHistory entity = this.importService.updateImportHistory(id, request);
        ImportHistoryResponse response = importService.toImportHistoryResponse(entity);
        return ResponseEntity.ok(response);
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyRole('admin')")
    // @ApiMessage("Xóa lịch sử nhập hàng thành công")
    // public ResponseEntity<Void> deleteImportHistory(@PathVariable Long id) {
    //     this.importService.deleteImportHistory(id);
    //     return ResponseEntity.noContent().build();
    // }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiMessage("Lấy danh sách lịch sử nhập hàng theo nhà cung cấp thành công")
    public ResponseEntity<List<ImportHistoryResponse>> getImportHistoriesBySupplier(@PathVariable("supplierId") Long supplierId) {
        List<ImportHistory> entities = importService.getImportHistoriesBySupplier(supplierId);
        List<ImportHistoryResponse> responses = importService.toImportHistoryResponseList(entities);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Upload file Excel để nhập hàng")
    public ResponseEntity<ImportExcelResponse> uploadImportExcel(
            @RequestParam("file") MultipartFile file) {
        try {
            // Kiểm tra file
            if (file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(ImportExcelResponse.error("File không được để trống"));
            }
            
            // Kiểm tra định dạng file
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls")) {
                return ResponseEntity
                        .badRequest()
                        .body(ImportExcelResponse.error("File phải có định dạng Excel (.xlsx hoặc .xls)"));
            }
            
            // Xử lý file Excel
            ImportHistory importHistory = importExcelService.processImportExcel(file);
            
            return ResponseEntity.ok(
                ImportExcelResponse.success(importHistory.getId(), importHistory.getTotalPrice())
            );
            
        } catch (Exception e) {
            log.error("Lỗi khi xử lý file Excel: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ImportExcelResponse.error("Lỗi khi xử lý file: " + e.getMessage()));
        }
    }
} 