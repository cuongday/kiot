package com.qad.posbe.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.Shift;
import com.qad.posbe.domain.User;
import com.qad.posbe.domain.request.CloseShiftDTO;
import com.qad.posbe.domain.request.OpenShiftDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.domain.response.ShiftResponse;
import com.qad.posbe.repository.OrderRepository;
import com.qad.posbe.repository.ShiftRepository;
import com.qad.posbe.util.constant.PaymentMethod;
import com.qad.posbe.util.constant.PaymentStatus;
import com.qad.posbe.util.constant.ShiftStatus;
import com.qad.posbe.util.error.BadRequestException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;

    /**
     * Mở ca làm việc mới
     * @param openShiftDTO Thông tin mở ca
     * @return Thông tin ca làm việc đã mở
     */
    @Transactional
    public ShiftResponse openShift(OpenShiftDTO openShiftDTO) {
        // Lấy thông tin người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        
        // Kiểm tra xem người dùng có ca làm việc đang mở không
        Optional<Shift> existingOpenShift = shiftRepository.findByUserAndStatus(currentUser, ShiftStatus.OPEN);
        if (existingOpenShift.isPresent()) {
            throw new BadRequestException("Bạn đang có ca làm việc mở. Vui lòng đóng ca hiện tại trước khi mở ca mới.");
        }
        
        // Tạo ca làm việc mới
        Shift newShift = Shift.builder()
                .user(currentUser)
                .startTime(Instant.now())
                .startCash(openShiftDTO.getStartCash())
                .status(ShiftStatus.OPEN)
                .expectedCash(openShiftDTO.getStartCash()) // Ban đầu, tiền dự kiến bằng tiền đầu ca
                .build();
        
        Shift savedShift = shiftRepository.save(newShift);
        
        return convertToShiftResponse(savedShift);
    }
    
    /**
     * Đóng ca làm việc
     * @param shiftId ID ca làm việc cần đóng
     * @param closeShiftDTO Thông tin đóng ca
     * @return Thông tin ca làm việc đã đóng
     */
    @Transactional
    public ShiftResponse closeShift(Long shiftId, CloseShiftDTO closeShiftDTO) {
        // Lấy thông tin ca làm việc
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ca làm việc với ID: " + shiftId));
        
        // Kiểm tra xem ca làm việc có phải là của người dùng hiện tại không
        User currentUser = userService.getCurrentUser();
        if (!shift.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Bạn không có quyền đóng ca làm việc này.");
        }
        
        // Kiểm tra trạng thái ca làm việc
        if (shift.getStatus() == ShiftStatus.CLOSED) {
            throw new BadRequestException("Ca làm việc này đã được đóng.");
        }
        
        // Tính toán doanh thu trong ca làm việc
        List<Order> ordersInShift = getOrdersInShift(shift);
        
        // Tính doanh thu tiền mặt và chuyển khoản
        long cashRevenue = ordersInShift.stream()
                .filter(order -> order.getPaymentMethod() == PaymentMethod.CASH && order.getPaymentStatus() == PaymentStatus.PAID)
                .mapToLong(Order::getTotalPrice)
                .sum();
        
        long transferRevenue = ordersInShift.stream()
                .filter(order -> order.getPaymentMethod() == PaymentMethod.TRANSFER && order.getPaymentStatus() == PaymentStatus.PAID)
                .mapToLong(Order::getTotalPrice)
                .sum();
        
        // Tính tiền dự kiến trong két = tiền đầu ca + doanh thu tiền mặt
        long expectedCash = shift.getStartCash() + cashRevenue;
        
        // Kiểm tra số tiền cuối ca có khớp với số tiền dự kiến không
        if (closeShiftDTO.getEndCash() != expectedCash) {
            throw new BadRequestException("Số tiền mặt đếm được (" + closeShiftDTO.getEndCash() + " đồng) không bằng số tiền mặt trong két (" + expectedCash + " đồng). Vui lòng kiểm tra lại!");
        }
        
        // Cập nhật thông tin ca làm việc
        shift.setEndTime(Instant.now());
        shift.setEndCash(closeShiftDTO.getEndCash());
        shift.setExpectedCash(expectedCash);
        shift.setStatus(ShiftStatus.CLOSED);
        shift.setNote(closeShiftDTO.getNote());
        
        Shift savedShift = shiftRepository.save(shift);
        
        // Tạo response với thêm thông tin về doanh thu
        ShiftResponse response = convertToShiftResponse(savedShift);
        response.setCashRevenue(cashRevenue);
        response.setTransferRevenue(transferRevenue);
        response.setTotalRevenue(cashRevenue + transferRevenue);
        response.setOrderCount(ordersInShift.size());
        response.setCashDifference(closeShiftDTO.getEndCash() - expectedCash);
        
        return response;
    }
    
    /**
     * Đóng ca làm việc hiện tại của user đang đăng nhập
     * @param closeShiftDTO Thông tin đóng ca
     * @return Thông tin ca làm việc đã đóng
     */
    @Transactional
    public ShiftResponse closeCurrentShift(CloseShiftDTO closeShiftDTO) {
        // Lấy thông tin người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        
        // Tìm ca làm việc đang mở của người dùng hiện tại
        Shift shift = shiftRepository.findByUserAndStatus(currentUser, ShiftStatus.OPEN)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ca làm việc nào đang mở của bạn."));
        
        // Tính toán doanh thu trong ca làm việc
        List<Order> ordersInShift = getOrdersInShift(shift);
        
        // Tính doanh thu tiền mặt và chuyển khoản
        long cashRevenue = ordersInShift.stream()
                .filter(order -> order.getPaymentMethod() == PaymentMethod.CASH && order.getPaymentStatus() == PaymentStatus.PAID)
                .mapToLong(Order::getTotalPrice)
                .sum();
        
        long transferRevenue = ordersInShift.stream()
                .filter(order -> order.getPaymentMethod() == PaymentMethod.TRANSFER && order.getPaymentStatus() == PaymentStatus.PAID)
                .mapToLong(Order::getTotalPrice)
                .sum();
        
        // Tính tiền dự kiến trong két = tiền đầu ca + doanh thu tiền mặt
        long expectedCash = shift.getStartCash() + cashRevenue;
        
        // Kiểm tra số tiền cuối ca có khớp với số tiền dự kiến không
        if (closeShiftDTO.getEndCash() != expectedCash) {
            throw new BadRequestException("Số tiền mặt đếm được (" + closeShiftDTO.getEndCash() + " đồng) không bằng số tiền mặt trong két (" + expectedCash + " đồng). Vui lòng kiểm tra lại!");
        }
        
        // Cập nhật thông tin ca làm việc
        shift.setEndTime(Instant.now());
        shift.setEndCash(closeShiftDTO.getEndCash());
        shift.setExpectedCash(expectedCash);
        shift.setStatus(ShiftStatus.CLOSED);
        shift.setNote(closeShiftDTO.getNote());
        
        Shift savedShift = shiftRepository.save(shift);
        
        // Tạo response với thêm thông tin về doanh thu
        ShiftResponse response = convertToShiftResponse(savedShift);
        response.setCashRevenue(cashRevenue);
        response.setTransferRevenue(transferRevenue);
        response.setTotalRevenue(cashRevenue + transferRevenue);
        response.setOrderCount(ordersInShift.size());
        response.setCashDifference(closeShiftDTO.getEndCash() - expectedCash);
        
        return response;
    }
    
    /**
     * Lấy thông tin ca làm việc hiện tại của user
     * @return Thông tin ca làm việc
     */
    public ShiftResponse getCurrentShift() {
        User currentUser = userService.getCurrentUser();
        Shift currentShift = shiftRepository.findByUserAndStatus(currentUser, ShiftStatus.OPEN)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ca làm việc nào đang mở."));
        
        return getShiftWithStats(currentShift);
    }
    
    /**
     * Kiểm tra xem có ca làm việc đang mở không
     * @return true nếu có ca đang mở
     */
    public boolean hasOpenShift() {
        User currentUser = userService.getCurrentUser();
        return shiftRepository.existsByUserAndStatus(currentUser, ShiftStatus.OPEN);
    }
    
    /**
     * Lấy thông tin chi tiết ca làm việc
     * @param id ID ca làm việc
     * @return Thông tin ca làm việc
     */
    public ShiftResponse getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ca làm việc với ID: " + id));
        
        return getShiftWithStats(shift);
    }
    
    /**
     * Lấy danh sách ca làm việc có phân trang
     * @param spec Điều kiện lọc
     * @param pageable Thông tin phân trang
     * @return Danh sách ca làm việc
     */
    public ResultPaginationDTO getAllShifts(Specification<Shift> spec, Pageable pageable) {
        Page<Shift> shiftPage = shiftRepository.findAll(spec, pageable);
        
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(shiftPage.getTotalPages());
        meta.setTotal(shiftPage.getTotalElements());
        
        result.setMeta(meta);
        
        List<ShiftResponse> shiftResponses = shiftPage.getContent().stream()
                .map(this::convertToShiftResponse)
                .collect(Collectors.toList());
        
        result.setResult(shiftResponses);
        
        return result;
    }
    
    /**
     * Lấy các đơn hàng trong ca làm việc
     * @param shift Ca làm việc
     * @return Danh sách đơn hàng
     */
    private List<Order> getOrdersInShift(Shift shift) {
        // Lấy thời gian bắt đầu ca
        Instant startTime = shift.getStartTime();
        
        // Thời gian kết thúc ca là thời điểm hiện tại nếu chưa đóng ca
        Instant endTime = (shift.getEndTime() != null) ? shift.getEndTime() : Instant.now();
        
        // Lấy danh sách đơn hàng trong khoảng thời gian ca làm việc của user hiện tại
        return orderRepository.findByCreatedAtBetween(startTime, endTime).stream()
                .filter(order -> order.getUser().getId().equals(shift.getUser().getId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Tính toán và thêm thông tin thống kê cho ca làm việc
     * @param shift Ca làm việc cần thêm thông tin
     * @return ShiftResponse với đầy đủ thông tin
     */
    private ShiftResponse getShiftWithStats(Shift shift) {
        ShiftResponse response = convertToShiftResponse(shift);
        
        // Chỉ tính thêm thông tin thống kê nếu ca đã mở
        if (shift != null) {
            List<Order> ordersInShift = getOrdersInShift(shift);
            
            // Tính doanh thu tiền mặt và chuyển khoản
            long cashRevenue = ordersInShift.stream()
                    .filter(order -> order.getPaymentMethod() == PaymentMethod.CASH && order.getPaymentStatus() == PaymentStatus.PAID)
                    .mapToLong(Order::getTotalPrice)
                    .sum();
            
            long transferRevenue = ordersInShift.stream()
                    .filter(order -> order.getPaymentMethod() == PaymentMethod.TRANSFER && order.getPaymentStatus() == PaymentStatus.PAID)
                    .mapToLong(Order::getTotalPrice)
                    .sum();
            
            // Cập nhật thông tin response
            response.setCashRevenue(cashRevenue);
            response.setTransferRevenue(transferRevenue);
            response.setTotalRevenue(cashRevenue + transferRevenue);
            response.setOrderCount(ordersInShift.size());
            
            // Tính tiền dự kiến trong két = tiền đầu ca + doanh thu tiền mặt
            long expectedCash = shift.getStartCash() + cashRevenue;
            response.setExpectedCash(expectedCash);
            
            // Tính chênh lệch nếu ca đã đóng
            if (shift.getStatus() == ShiftStatus.CLOSED && shift.getEndCash() != 0) {
                response.setCashDifference(shift.getEndCash() - expectedCash);
            }
        }
        
        return response;
    }
    
    /**
     * Chuyển đổi từ entity Shift sang ShiftResponse
     * @param shift Entity cần chuyển đổi
     * @return ShiftResponse
     */
    private ShiftResponse convertToShiftResponse(Shift shift) {
        if (shift == null) {
            return null;
        }
        
        ShiftResponse.UserInfoResponse userInfo = null;
        if (shift.getUser() != null) {
            userInfo = ShiftResponse.UserInfoResponse.builder()
                    .id(shift.getUser().getId())
                    .name(shift.getUser().getName())
                    .username(shift.getUser().getUsername())
                    .build();
        }
        
        return ShiftResponse.builder()
                .id(shift.getId())
                .user(userInfo)
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .startCash(shift.getStartCash())
                .endCash(shift.getEndCash())
                .expectedCash(shift.getExpectedCash())
                .status(shift.getStatus())
                .note(shift.getNote())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
} 