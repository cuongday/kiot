package com.qad.posbe.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.qad.posbe.service.PdfExportService;
import com.qad.posbe.service.UserService;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.constant.PaymentMethod;
import com.qad.posbe.util.constant.PaymentStatus;
import com.turkraft.springfilter.boot.Filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final PdfExportService pdfExportService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    
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
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
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
    
    @GetMapping(value = "/{id}/invoice", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Xuất hóa đơn PDF thành công")
    public ResponseEntity<Resource> generateInvoice(@PathVariable("id") Long id) {
        try {
            // Kiểm tra đơn hàng tồn tại
            orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
            
            // Tạo hóa đơn PDF
            byte[] pdfContent = pdfExportService.generateInvoicePdf(id);
            
            if (pdfContent == null || pdfContent.length == 0) {
                throw new RuntimeException("Không thể tạo nội dung PDF cho đơn hàng ID: " + id);
            }
            
            // Tạo resource từ byte array
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            
            // Thiết lập headers cho phản hồi
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf");
            
            // Trả về response với resource PDF
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfContent.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            // Log lỗi và ném ngoại lệ
            log.error("Lỗi khi tạo hóa đơn PDF cho đơn hàng ID: {}: {}", id, e.getMessage());
            throw new RuntimeException("Lỗi khi tạo hóa đơn PDF: " + e.getMessage(), e);
        }
    }

    // Thêm API để kiểm tra tình trạng của chức năng xuất PDF
    @GetMapping("/check-pdf-generation")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, Object>> checkPdfGeneration() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Kiểm tra iText đã được cấu hình đúng
            boolean fontSupported = pdfExportService.checkFontAvailability();
            
            result.put("success", true);
            result.put("fontSupported", fontSupported);
            result.put("fontInfo", pdfExportService.getFontInfo());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getName());
            log.error("Lỗi khi kiểm tra chức năng xuất PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
} 