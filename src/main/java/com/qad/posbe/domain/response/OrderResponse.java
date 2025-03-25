package com.qad.posbe.domain.response;

import com.qad.posbe.domain.Order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Order order;
    private String paymentUrl;
}
