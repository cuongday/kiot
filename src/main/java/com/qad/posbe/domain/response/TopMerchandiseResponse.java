package com.qad.posbe.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopMerchandiseResponse {
    private Long id;
    private String name;
    private long value;
} 