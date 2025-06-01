package com.qad.posbe.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qad.posbe.domain.Customer;
import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.User;
import com.qad.posbe.domain.request.CreateOrderDTO;
import com.qad.posbe.domain.request.OrderItemDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.repository.CustomerRepository;
import com.qad.posbe.repository.OrderDetailRepository;
import com.qad.posbe.repository.OrderRepository;
import com.qad.posbe.repository.ProductRepository;
import com.qad.posbe.repository.UserRepository;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.constant.PaymentStatus;
import com.qad.posbe.util.constant.ProductStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ShiftService shiftService;

    @Transactional
    public Order createOrder(CreateOrderDTO createOrderDTO) {
        // Kiểm tra ca làm việc có mở không
        if (!shiftService.hasOpenShift()) {
            throw new com.qad.posbe.util.error.BusinessException(
                "SHIFT_NOT_OPEN", 
                "Không thể tạo đơn hàng khi chưa mở ca làm việc. Vui lòng mở ca trước khi bán hàng."
            );
        }
        
        // Get current user
        String currentUsername = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new com.qad.posbe.util.error.BusinessException(
                    "USER_NOT_FOUND", "Không tìm thấy người dùng hiện tại"));
        User currentUser = this.userRepository.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new com.qad.posbe.util.error.BusinessException(
                "USER_NOT_FOUND", "Không tìm thấy người dùng hiện tại");
        }
        
        // Get customer if provided
        Customer customer = null;
        if (createOrderDTO.getCustomerId() != null) {
            customer = this.customerRepository.findById(createOrderDTO.getCustomerId())
                    .orElseThrow(() -> new com.qad.posbe.util.error.BusinessException(
                        "CUSTOMER_NOT_FOUND", "Không tìm thấy khách hàng"));
        }
        
        // Pre-load all products to avoid multiple database calls
        List<Long> productIds = createOrderDTO.getItems().stream()
                .map(OrderItemDTO::getProductId)
                .toList();
                
        List<Product> products = this.productRepository.findAllById(productIds);
        
        // Create a map for quick access to products
        var productMap = products.stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, p -> p));
        
        // Validate products and calculate total price
        long totalPrice = 0;
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product == null) {
                throw new com.qad.posbe.util.error.BusinessException(
                    "PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm với ID: " + item.getProductId());
            }
            
            // Kiểm tra trạng thái sản phẩm
            if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
                throw new com.qad.posbe.util.error.BusinessException(
                    "PRODUCT_OUT_OF_STOCK", "Không thể bán sản phẩm '" + product.getName() + "' vì đã hết hàng");
            }
            
            if (product.getStatus() == ProductStatus.INACTIVE) {
                throw new com.qad.posbe.util.error.BusinessException(
                    "PRODUCT_INACTIVE", "Không thể bán sản phẩm '" + product.getName() + "' vì đã ngừng kinh doanh");
            }
            
            // Kiểm tra số lượng tồn kho
            if (product.getQuantity() < item.getQuantity()) {
                throw new com.qad.posbe.util.error.BusinessException(
                    "INSUFFICIENT_STOCK", "Số lượng sản phẩm '" + product.getName() + "' không đủ. Còn lại: " + product.getQuantity());
            }
            
            // Use the sellPrice from OrderItemDTO instead of product
            long itemPrice = item.getSellPrice() * item.getQuantity();
            
            // Debug log for price calculation
            System.out.println("Product ID: " + item.getProductId() + 
                               ", SellPrice: " + item.getSellPrice() + 
                               ", Quantity: " + item.getQuantity() + 
                               ", ItemTotal: " + itemPrice);
            
            totalPrice += itemPrice;
        }
        
        // Debug total price
        System.out.println("Final Total Price: " + totalPrice);
        
        // Create and save order
        Order order = Order.builder()
                .paymentMethod(createOrderDTO.getPaymentMethod())
                .paymentStatus(PaymentStatus.PAID)
                .totalPrice(totalPrice)
                .user(currentUser)
                .customer(customer)
                .createdAt(Instant.now())
                .createdBy(currentUsername)
                .build();
        
        Order savedOrder = this.orderRepository.save(order);
        
        // Create and save order details
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            Product product = productMap.get(item.getProductId());
            
            // Update product stock
            int currentStock = product.getQuantity();
            product.setQuantity(currentStock - item.getQuantity());
            productRepository.save(product);
            
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .product(product)
                    // Use sellPrice from OrderItemDTO
                    .price(item.getSellPrice())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getSellPrice() * item.getQuantity())
                    .createdAt(Instant.now())
                    .createdBy(currentUsername)
                    .build();
            
            orderDetails.add(orderDetailRepository.save(orderDetail));
        }
        
        return savedOrder;
    }
    
    /**
     * Update an existing order
     * 
     * @param order Order to update
     * @return Updated order
     */
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    public ResultPaginationDTO getAllOrders(Specification<Order> spec, Pageable pageable) {
        // Xử lý lại pageNumber
        int pageNumber = pageable.getPageNumber();
        if (pageNumber > 0) {
            pageNumber = pageNumber - 1;
        }
        
        // Tạo lại pageable với pageNumber mới
        Pageable adjustedPageable = PageRequest.of(pageNumber, pageable.getPageSize(), pageable.getSort());
        
        // Lấy danh sách đơn hàng với pageable đã điều chỉnh
        Page<Order> orderPage = orderRepository.findAll(spec, adjustedPageable);
        
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageNumber + 1); // Trả về số trang theo format của client
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(orderPage.getTotalPages());
        meta.setTotal(orderPage.getTotalElements());
        
        result.setMeta(meta);
        result.setResult(orderPage.getContent());
        
        return result;
    }
    
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
    
    public List<OrderDetail> getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
        return order.getOrderDetails();
    }
} 