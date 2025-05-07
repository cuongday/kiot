package com.qad.posbe.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenShiftDTO {
    @NotNull(message = "Số tiền đầu ca không được để trống")
    @Min(value = 0, message = "Số tiền đầu ca phải lớn hơn hoặc bằng 0")
    private Long startCash;
} 