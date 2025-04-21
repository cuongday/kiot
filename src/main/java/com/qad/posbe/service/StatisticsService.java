package com.qad.posbe.service;

import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.response.StatisticsResponse;
import com.qad.posbe.domain.response.StatisticsResponse.StatisticsItemResponse;
import com.qad.posbe.domain.response.TodayStatisticsResponse;
import com.qad.posbe.domain.response.TodayStatisticsResponse.CompareData;
import com.qad.posbe.domain.response.TopMerchandiseResponse;
import com.qad.posbe.repository.OrderDetailRepository;
import com.qad.posbe.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final OrderRepository orderRepository;

    public StatisticsResponse getRevenueStatistics(int month, int year, String type) {
        // Xác định khoảng thời gian cần thống kê
        YearMonth yearMonth = YearMonth.of(year, month);
        // Sử dụng ZoneId.of("Asia/Ho_Chi_Minh") để đảm bảo đúng múi giờ GMT+7
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        
        // Chuyển đổi LocalDateTime sang Instant với múi giờ Việt Nam (GMT+7)
        Instant startInstant = startOfMonth.atZone(vietnamZone).toInstant();
        Instant endInstant = endOfMonth.atZone(vietnamZone).toInstant();

        // Lấy tất cả đơn hàng trong tháng đã chỉ định
        List<Order> orders = this.orderRepository.findByCreatedAtBetween(startInstant, endInstant);

        // Xử lý theo loại thống kê
        switch (type) {
            case "daily":
                return getDailyStatistics(orders, yearMonth);
            case "hourly":
                return getHourlyStatistics(orders);
            case "weekly":
                return getWeeklyStatistics(orders);
            default:
                throw new IllegalArgumentException("Loại thống kê không hợp lệ: " + type);
        }
    }

    private StatisticsResponse getDailyStatistics(List<Order> orders, YearMonth yearMonth) {
        int daysInMonth = yearMonth.lengthOfMonth();
        
        // Tạo map để tính tổng doanh thu cho mỗi ngày
        Map<Integer, Long> dailyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.systemDefault()).getDayOfMonth(),
                        Collectors.summingLong(Order::getTotalPrice)
                ));
        
        // Tạo danh sách kết quả cho tất cả các ngày trong tháng
        List<StatisticsItemResponse> data = IntStream.rangeClosed(1, daysInMonth)
                .mapToObj(day -> StatisticsItemResponse.builder()
                        .label(String.format("%02d", day))
                        .value(dailyRevenue.getOrDefault(day, 0L))
                        .build())
                .collect(Collectors.toList());
        
        return StatisticsResponse.builder()
                .type("daily")
                .data(data)
                .build();
    }

    private StatisticsResponse getHourlyStatistics(List<Order> orders) {
        // Tạo map để tính tổng doanh thu cho mỗi giờ trong ngày
        Map<Integer, Long> hourlyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.systemDefault()).getHour(),
                        Collectors.summingLong(Order::getTotalPrice)
                ));
        
        // Tạo danh sách kết quả cho tất cả các giờ trong ngày (0-23)
        List<StatisticsItemResponse> data = IntStream.rangeClosed(0, 23)
                .mapToObj(hour -> StatisticsItemResponse.builder()
                        .label(String.format("%02d", hour))
                        .value(hourlyRevenue.getOrDefault(hour, 0L))
                        .build())
                .collect(Collectors.toList());
        
        return StatisticsResponse.builder()
                .type("hourly")
                .data(data)
                .build();
    }

    private StatisticsResponse getWeeklyStatistics(List<Order> orders) {
        // Tạo map để tính tổng doanh thu cho mỗi ngày trong tuần (1-7, thứ 2 đến chủ nhật)
        Map<Integer, Long> weeklyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.systemDefault()).getDayOfWeek().getValue(),
                        Collectors.summingLong(Order::getTotalPrice)
                ));
        
        // Tạo danh sách kết quả cho tất cả các ngày trong tuần
        List<StatisticsItemResponse> data = IntStream.rangeClosed(1, 7)
                .mapToObj(dayOfWeek -> {
                    String dayName = DayOfWeek.of(dayOfWeek).getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));
                    return StatisticsItemResponse.builder()
                            .label(dayName)
                            .value(weeklyRevenue.getOrDefault(dayOfWeek, 0L))
                            .build();
                })
                .collect(Collectors.toList());
        
        return StatisticsResponse.builder()
                .type("weekly")
                .data(data)
                .build();
    }

    public TodayStatisticsResponse getTodayStatistics() {
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDate today = LocalDate.now(vietnamZone);
        
        // Lấy dữ liệu hôm nay
        Instant startToday = today.atStartOfDay(vietnamZone).toInstant();
        Instant endToday = today.atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
        List<Order> todayOrders = orderRepository.findByCreatedAtBetween(startToday, endToday);
        
        long todayRevenue = todayOrders.stream().mapToLong(Order::getTotalPrice).sum();
        int todayOrderCount = todayOrders.size();
        
        // Lấy dữ liệu hôm qua
        LocalDate yesterday = today.minusDays(1);
        Instant startYesterday = yesterday.atStartOfDay(vietnamZone).toInstant();
        Instant endYesterday = yesterday.atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
        List<Order> yesterdayOrders = orderRepository.findByCreatedAtBetween(startYesterday, endYesterday);
        
        long yesterdayRevenue = yesterdayOrders.stream().mapToLong(Order::getTotalPrice).sum();
        int yesterdayOrderCount = yesterdayOrders.size();
        
        // Lấy dữ liệu cùng ngày tháng trước
        LocalDate lastMonth = today.minusMonths(1);
        Instant startLastMonth = lastMonth.atStartOfDay(vietnamZone).toInstant();
        Instant endLastMonth = lastMonth.atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
        List<Order> lastMonthOrders = orderRepository.findByCreatedAtBetween(startLastMonth, endLastMonth);
        
        long lastMonthRevenue = lastMonthOrders.stream().mapToLong(Order::getTotalPrice).sum();
        int lastMonthOrderCount = lastMonthOrders.size();
        
        // Tính toán phần trăm so sánh doanh thu
        CompareData compareYesterday = calculateCompareData(todayRevenue, yesterdayRevenue);
        CompareData compareLastMonth = calculateCompareData(todayRevenue, lastMonthRevenue);
        
        return TodayStatisticsResponse.builder()
                .value(todayRevenue)
                .count(todayOrderCount)
                .compareYesterday(compareYesterday)
                .compareLastMonth(compareLastMonth)
                .build();
    }
    
    private CompareData calculateCompareData(long currentValue, long previousValue) {
        double percentage = 0;
        String trend = "equal";
        
        if (previousValue > 0) {
            percentage = ((double) (currentValue - previousValue) / previousValue) * 100;
            percentage = Math.round(percentage * 100.0) / 100.0; // Làm tròn đến 2 chữ số thập phân
        } else if (currentValue > 0) {
            percentage = 100.0;
        }
        
        if (currentValue > previousValue) {
            trend = "up";
        } else if (currentValue < previousValue) {
            trend = "down";
        }
        
        return CompareData.builder()
                .percentage(percentage)
                .trend(trend)
                .build();
    }
    
    public List<TopMerchandiseResponse> getTopMerchandise(String timeframe, String metric, int limit) {
        // Xác định khoảng thời gian cần thống kê
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDate now = LocalDate.now(vietnamZone);
        Instant startInstant, endInstant;
        
        switch (timeframe) {
            case "today":
                startInstant = now.atStartOfDay(vietnamZone).toInstant();
                endInstant = now.atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
                break;
            case "week":
                // Lấy đầu tuần (Thứ Hai) và cuối tuần (Chủ Nhật)
                LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                startInstant = startOfWeek.atStartOfDay(vietnamZone).toInstant();
                endInstant = endOfWeek.atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
                break;
            case "month":
                // Lấy đầu tháng và cuối tháng
                YearMonth yearMonth = YearMonth.from(now);
                startInstant = yearMonth.atDay(1).atStartOfDay(vietnamZone).toInstant();
                endInstant = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
                break;
            case "year":
                // Lấy đầu năm và cuối năm
                int year = now.getYear();
                startInstant = LocalDate.of(year, 1, 1).atStartOfDay(vietnamZone).toInstant();
                endInstant = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX).atZone(vietnamZone).toInstant();
                break;
            default:
                throw new IllegalArgumentException("Khoảng thời gian không hợp lệ: " + timeframe);
        }
        
        // Lấy tất cả đơn hàng trong khoảng thời gian đã chỉ định
        List<Order> orders = orderRepository.findByCreatedAtBetween(startInstant, endInstant);
        
        // Lấy chi tiết của tất cả đơn hàng
        List<OrderDetail> allOrderDetails = new ArrayList<>();
        for (Order order : orders) {
            allOrderDetails.addAll(order.getOrderDetails());
        }
        
        // Tính toán theo loại thống kê (doanh thu hoặc số lượng)
        Map<Product, Long> productValueMap = new HashMap<>();
        
        if ("revenue".equals(metric)) {
            // Tính tổng doanh thu theo sản phẩm
            for (OrderDetail detail : allOrderDetails) {
                Product product = detail.getProduct();
                long currentRevenue = productValueMap.getOrDefault(product, 0L);
                productValueMap.put(product, currentRevenue + detail.getTotalPrice());
            }
        } else if ("quantity".equals(metric)) {
            // Tính tổng số lượng bán ra theo sản phẩm
            for (OrderDetail detail : allOrderDetails) {
                Product product = detail.getProduct();
                long currentQuantity = productValueMap.getOrDefault(product, 0L);
                productValueMap.put(product, currentQuantity + detail.getQuantity());
            }
        } else {
            throw new IllegalArgumentException("Loại thống kê không hợp lệ: " + metric);
        }
        
        // Sắp xếp và lấy top sản phẩm
        return productValueMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> TopMerchandiseResponse.builder()
                        .id(entry.getKey().getId())
                        .name(entry.getKey().getName())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}