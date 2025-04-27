package com.qad.posbe.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportExcelDTO {
    private Long supplierId;
    @Builder.Default
    private List<ImportProductDTO> products = new ArrayList<>();
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImportProductDTO {
        private Long productId;
        private String productName;
        private int quantity;
    }
} 