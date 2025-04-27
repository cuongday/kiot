package com.qad.posbe.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.domain.User;
import com.qad.posbe.domain.request.CreateOrderDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.service.OrderService;
import com.qad.posbe.service.UserService;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.constant.PaymentMethod;
import com.qad.posbe.util.constant.PaymentStatus;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @PostMapping
    @ApiMessage("Tạo đơn hàng mới thành công")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        // Debug log for incoming DTO
        System.out.println("Received order request: " + createOrderDTO);
        System.out.println("Payment method: " + createOrderDTO.getPaymentMethod());
        System.out.println("Items count: " + createOrderDTO.getItems().size());
        
        // Log each item
        for (int i = 0; i < createOrderDTO.getItems().size(); i++) {
            var item = createOrderDTO.getItems().get(i);
            System.out.println("Item " + i + ": ProductID=" + item.getProductId() + 
                               ", Quantity=" + item.getQuantity() + 
                               ", SellPrice=" + item.getSellPrice());
        }
        
        Order order = this.orderService.createOrder(createOrderDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping
    @ApiMessage("Lấy danh sách đơn hàng thành công")
    public ResponseEntity<ResultPaginationDTO> getAllOrders(
            @Filter Specification<Order> orderSpec,
            Pageable pageable) {
        
        ResultPaginationDTO result = this.orderService.getAllOrders(orderSpec, pageable);
        return ResponseEntity.ok(result);
    }
    
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/{id}")
    @ApiMessage("Lấy thông tin đơn hàng thành công")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy danh sách đơn hàng của tôi thành công")
    public ResponseEntity<List<Order>> getMyOrders() {
        String currentUsername = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng hiện tại"));
        
        User currentUser = userService.handleGetUserByUserName(currentUsername);
        List<Order> myOrders = orderService.getOrdersByUser(currentUser);
        
        return ResponseEntity.ok(myOrders);
    }
    
    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Lấy chi tiết đơn hàng thành công")
    public ResponseEntity<List<OrderDetail>> getOrderDetails(@PathVariable("id") Long id) {
        List<OrderDetail> orderDetails = orderService.getOrderDetails(id);
        return ResponseEntity.ok(orderDetails);
    }
    
    
    @PostMapping("/{id}/update-payment-status")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Cập nhật trạng thái thanh toán thành công")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable("id") Long id,
            @RequestParam("paymentStatus") PaymentStatus paymentStatus) {
        
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
        
        // Update payment status
        order.setPaymentStatus(paymentStatus);
        
        // Add payment message
        if (paymentStatus == PaymentStatus.PAID) {
            order.setPaymentMessage("Thanh toán thành công");
        } else if (paymentStatus == PaymentStatus.FAILED) {
            order.setPaymentMessage("Thanh toán thất bại");
        } else if (paymentStatus == PaymentStatus.PENDING) {
            order.setPaymentMessage("Đang chờ thanh toán");
        } else if (paymentStatus == PaymentStatus.REFUNDED) {
            order.setPaymentMessage("Đã hoàn tiền");
        }
        
        // Save updated order
        Order updatedOrder = orderService.updateOrder(order);
        
        return ResponseEntity.ok(updatedOrder);
    }
} 