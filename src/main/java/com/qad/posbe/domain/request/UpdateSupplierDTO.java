package com.qad.posbe.domain.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSupplierDTO {
    @Size(min = 2, message = "Tên nhà cung cấp phải có ít nhất 2 ký tự")
    private String name;
    
    private String description;
} 