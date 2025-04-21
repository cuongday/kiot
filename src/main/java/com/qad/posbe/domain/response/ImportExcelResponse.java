package com.qad.posbe.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportExcelResponse {
    private String message;
    private Long importId;
    private Long totalPrice;
    private String errorMessage;
    
    public static ImportExcelResponse success(Long importId, Long totalPrice) {
        return ImportExcelResponse.builder()
                .message("Nhập hàng thành công")
                .importId(importId)
                .totalPrice(totalPrice)
                .build();
    }
    
    public static ImportExcelResponse error(String errorMessage) {
        return ImportExcelResponse.builder()
                .message("Lỗi")
                .errorMessage(errorMessage)
                .build();
    }
} 