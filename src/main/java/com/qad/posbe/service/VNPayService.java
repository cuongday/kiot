package com.qad.posbe.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.qad.posbe.config.VNPayConfig;
import com.qad.posbe.domain.Order;
import com.qad.posbe.repository.OrderRepository;
import com.qad.posbe.util.constant.PaymentStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService {
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;

    public String createPaymentUrl(Long orderId, long amount, String orderInfo) {
        return vnPayConfig.createPaymentUrl(orderId, amount, orderInfo);
    }

    public Optional<Order> processPaymentReturn(Map<String, String> vnpParams) {
        // Validate VNPay responsea
        if (!vnPayConfig.validateReturnData(vnpParams)) {
            log.error("Invalid checksum from VNPay");
            return Optional.empty();
        }

        // Get payment information
        String vnpResponseCode = vnpParams.get("vnp_ResponseCode");
        String orderId = vnpParams.get("vnp_TxnRef");

        // Find the order
        Optional<Order> orderOpt = orderRepository.findById(Long.parseLong(orderId));
        if (orderOpt.isEmpty()) {
            log.error("Order not found: {}", orderId);
            return Optional.empty();
        }

        Order order = orderOpt.get();

        // Check if payment is successful
        if ("00".equals(vnpResponseCode)) {
            // Update order status
            order.setPaymentStatus(PaymentStatus.PAID);
            
            // Add payment success message - sử dụng số nguyên thay vì định dạng
            String successMessage = "Người dùng đã thanh toán thành công " + order.getTotalPrice() + " đồng";
            order.setPaymentMessage(successMessage);
            
            // Save transaction number from VNPay if available
            if (vnpParams.containsKey("vnp_TransactionNo")) {
                order.setTransactionNo(vnpParams.get("vnp_TransactionNo"));
            }
            
            orderRepository.save(order);
            log.info("Payment successful for order: {}", orderId);
            return Optional.of(order);
        } else {
            // Payment failed
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setPaymentMessage("Thanh toán không thành công. Mã lỗi: " + vnpResponseCode);
            orderRepository.save(order);
            log.error("Payment failed for order: {} with response code: {}", orderId, vnpResponseCode);
            return Optional.empty();
        }
    }
} 