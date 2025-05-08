package com.qad.posbe.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.domain.Product;
import com.qad.posbe.repository.OrderRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfExportService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final ServletContext servletContext;
    
    // Font cache để tối ưu hiệu suất
    private Map<String, PdfFont> fontCache = new HashMap<>();
    
    @PostConstruct
    public void init() {
        try {
            log.info("Khởi tạo font cho PDF...");
            
            // Tải font Times New Roman từ classpath
            loadFontFromClassPath();
            
            log.info("Khởi tạo font thành công: {}", fontCache.keySet());
        } catch (Exception e) {
            log.error("Lỗi khi khởi tạo font: {}", e.getMessage(), e);
            // Thử sử dụng font mặc định
            registerDefaultFonts();
        }
    }
    
    private void loadFontFromClassPath() {
        try {
            // Đọc font từ tài nguyên trong classpath
            ClassPathResource resource = new ClassPathResource("fonts/times.ttf");
            
            if (resource.exists()) {
                log.info("Đã tìm thấy font Times New Roman trong classpath");
                
                try (InputStream is = resource.getInputStream()) {
                    // Đọc font từ stream
                    byte[] fontData = is.readAllBytes();
                    
                    // Tạo font từ dữ liệu byte
                    FontProgram fontProgram = FontProgramFactory.createFont(fontData);
                    
                    // Tạo font với encoding IDENTITY_H để hỗ trợ tiếng Việt
                    PdfFont normalFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
                    fontCache.put("normal", normalFont);
                    
                    // Sử dụng font đầm đặc hơn cho dữ liệu in đậm
                    PdfFont boldFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED);
                    fontCache.put("bold", boldFont);
                    
                    log.info("Đã tải font Times New Roman thành công");
                }
            } else {
                log.warn("Không tìm thấy font trong classpath, đang sử dụng font dự phòng");
                registerDefaultFonts();
            }
        } catch (Exception e) {
            log.error("Lỗi khi tải font từ classpath: {}", e.getMessage(), e);
            registerDefaultFonts();
        }
    }
    
    private void registerDefaultFonts() {
        try {
            log.info("Đang đăng ký font mặc định...");
            
            // Sử dụng font Helvetica với encoding CP1252 hỗ trợ tiếng Việt
            PdfFont normalFont = PdfFontFactory.createFont("Helvetica", PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
            fontCache.put("normal", normalFont);
            
            PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold", PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
            fontCache.put("bold", boldFont);
            
            log.info("Đã đăng ký font mặc định thành công");
        } catch (IOException e) {
            log.error("Lỗi khi đăng ký font mặc định: {}", e.getMessage(), e);
        }
    }
    
    // Lấy font từ cache
    private PdfFont getFont(boolean bold) {
        String key = bold ? "bold" : "normal";
        
        if (fontCache.containsKey(key)) {
            return fontCache.get(key);
        }
        
        try {
            log.warn("Font {} không có trong cache, đang tạo font mặc định", key);
            PdfFont font = PdfFontFactory.createFont("Helvetica", PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
            fontCache.put(key, font);
            return font;
        } catch (IOException e) {
            log.error("Lỗi khi tạo font {}: {}", key, e.getMessage());
            throw new RuntimeException("Không thể tạo font: " + e.getMessage());
        }
    }

    public byte[] generateInvoicePdf(Long orderId) {
        ByteArrayOutputStream outputStream = null;
        try {
            log.info("Bắt đầu tạo hóa đơn PDF cho đơn hàng ID: {}", orderId);
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
            
            log.info("Tìm thấy đơn hàng, đang lấy chi tiết...");
            List<OrderDetail> orderDetails = orderService.getOrderDetails(orderId);
            
            if (orderDetails == null || orderDetails.isEmpty()) {
                log.error("Không tìm thấy chi tiết đơn hàng cho ID: {}", orderId);
                throw new RuntimeException("Không tìm thấy chi tiết đơn hàng cho ID: " + orderId);
            }
            
            log.info("Đã tìm thấy {} sản phẩm trong đơn hàng", orderDetails.size());
            
            outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            
            // Sử dụng khổ giấy A5
            Document document = new Document(pdfDoc, PageSize.A5);
            
            // Lấy các font từ cache
            PdfFont normalFont = getFont(false);
            PdfFont boldFont = getFont(true);
            
            // Tạo tiêu đề hóa đơn
            Paragraph header = new Paragraph("CỬA HÀNG TẠP HÓA THÁI THỤY")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(boldFont)
                    .setFontSize(16);
            document.add(header);
            
            // Thêm địa chỉ và thông tin liên hệ
            Paragraph address = new Paragraph("Địa chỉ: 123 Đường Thái Thuỵ, Quận Thanh Xuân, Hà Nội")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(normalFont)
                    .setFontSize(10);
            document.add(address);
            
            Paragraph contact = new Paragraph("SĐT: 0123456789 - Email: taphothaitthuy@gmail.com")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(normalFont)
                    .setFontSize(10);
            document.add(contact);
            
            // Tạo tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setMarginTop(20);
            document.add(title);
            
            // Thêm thông tin hóa đơn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date orderDate = null;
            if (order.getCreatedAt() != null) {
                orderDate = Date.from(order.getCreatedAt());
            } else {
                orderDate = new Date(); // Sử dụng thời gian hiện tại nếu không có
                log.warn("Đơn hàng không có thời gian tạo, sử dụng thời gian hiện tại");
            }
            
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            // Thêm số hóa đơn
            infoTable.addCell(createCell("Số hóa đơn:", boldFont, TextAlignment.LEFT));
            infoTable.addCell(createCell("#" + order.getId(), normalFont, TextAlignment.RIGHT));
            
            // Thêm ngày tạo hóa đơn
            infoTable.addCell(createCell("Ngày:", boldFont, TextAlignment.LEFT));
            infoTable.addCell(createCell(dateFormat.format(orderDate), normalFont, TextAlignment.RIGHT));
            
            // Thêm thông tin khách hàng
            if (order.getCustomer() != null) {
                infoTable.addCell(createCell("Khách hàng:", boldFont, TextAlignment.LEFT));
                String customerName = order.getCustomer().getFullname() != null ? 
                    order.getCustomer().getFullname() : "Không có tên";
                infoTable.addCell(createCell(customerName, normalFont, TextAlignment.RIGHT));
                
                infoTable.addCell(createCell("Số điện thoại:", boldFont, TextAlignment.LEFT));
                String customerPhone = order.getCustomer().getPhone() != null ? 
                    order.getCustomer().getPhone() : "Không có số điện thoại";
                infoTable.addCell(createCell(customerPhone, normalFont, TextAlignment.RIGHT));
            } else {
                infoTable.addCell(createCell("Khách hàng:", boldFont, TextAlignment.LEFT));
                infoTable.addCell(createCell("Khách lẻ", normalFont, TextAlignment.RIGHT));
            }
            
            // Thêm thông tin người bán
            infoTable.addCell(createCell("Nhân viên:", boldFont, TextAlignment.LEFT));
            if (order.getUser() != null && order.getUser().getName() != null) {
                infoTable.addCell(createCell(order.getUser().getName(), normalFont, TextAlignment.RIGHT));
            } else {
                infoTable.addCell(createCell("Không xác định", normalFont, TextAlignment.RIGHT));
                log.warn("Không tìm thấy thông tin người bán cho đơn hàng ID: {}", orderId);
            }
            
            document.add(infoTable);
            
            // Thêm bảng danh sách sản phẩm
            Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 40, 15, 20, 20}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(15);
            
            // Tạo header cho bảng sản phẩm
            productTable.addCell(createHeaderCell("STT", boldFont));
            productTable.addCell(createHeaderCell("Sản phẩm", boldFont));
            productTable.addCell(createHeaderCell("SL", boldFont));
            productTable.addCell(createHeaderCell("Đơn giá", boldFont));
            productTable.addCell(createHeaderCell("Thành tiền", boldFont));
            
            // Thêm các sản phẩm vào bảng
            int index = 1;
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            
            for (OrderDetail detail : orderDetails) {
                productTable.addCell(createCell(String.valueOf(index++), normalFont, TextAlignment.CENTER));
                
                // Kiểm tra sản phẩm có tồn tại không
                Product product = detail.getProduct();
                String productName = "Sản phẩm không xác định";
                if (product != null && product.getName() != null) {
                    productName = product.getName();
                } else {
                    log.warn("Không tìm thấy thông tin sản phẩm cho chi tiết đơn hàng ID: {}", detail.getId());
                }
                productTable.addCell(createCell(productName, normalFont, TextAlignment.LEFT));
                
                productTable.addCell(createCell(String.valueOf(detail.getQuantity()), normalFont, TextAlignment.CENTER));
                productTable.addCell(createCell(priceFormat.format(detail.getPrice()) + "đ", normalFont, TextAlignment.RIGHT));
                productTable.addCell(createCell(priceFormat.format(detail.getTotalPrice()) + "đ", normalFont, TextAlignment.RIGHT));
            }
            
            document.add(productTable);
            
            // Thêm tổng tiền
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            summaryTable.addCell(createCellWithBorderRight("Tổng cộng:", boldFont, TextAlignment.RIGHT));
            summaryTable.addCell(createCellWithoutBorderLeft(priceFormat.format(order.getTotalPrice()) + "đ", boldFont, TextAlignment.RIGHT));
            
            document.add(summaryTable);
            
            // Thêm thông tin thanh toán
            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            paymentTable.addCell(createCell("Phương thức thanh toán:", boldFont, TextAlignment.LEFT));
            String paymentMethod = "Không xác định";
            if (order.getPaymentMethod() != null) {
                paymentMethod = order.getPaymentMethod().toString().equals("CASH") ? "Tiền mặt" : "Chuyển khoản";
            } else {
                log.warn("Không tìm thấy phương thức thanh toán cho đơn hàng ID: {}", orderId);
            }
            paymentTable.addCell(createCell(paymentMethod, normalFont, TextAlignment.RIGHT));
            
            paymentTable.addCell(createCell("Trạng thái thanh toán:", boldFont, TextAlignment.LEFT));
            String paymentStatus = "Không xác định";
            if (order.getPaymentStatus() != null) {
                paymentStatus = order.getPaymentStatus().toString().equals("PAID") ? "Đã thanh toán" : "Chưa thanh toán";
            } else {
                log.warn("Không tìm thấy trạng thái thanh toán cho đơn hàng ID: {}", orderId);
            }
            paymentTable.addCell(createCell(paymentStatus, normalFont, TextAlignment.RIGHT));
            
            document.add(paymentTable);
            
            // Thêm lời cảm ơn
            Paragraph thankYou = new Paragraph("Cảm ơn Quý khách đã mua hàng!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setMarginTop(20);
            document.add(thankYou);
            
            // Đóng tài liệu
            document.close();
            log.info("Đã tạo thành công hóa đơn PDF cho đơn hàng ID: {}", orderId);
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Lỗi khi tạo hóa đơn PDF cho đơn hàng ID: {}: {}", orderId, e.getMessage(), e);
            throw new RuntimeException("Lỗi khi tạo hóa đơn PDF: " + e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("Lỗi khi đóng outputStream: {}", e.getMessage());
                }
            }
        }
    }
    
    private Cell createCell(String content, PdfFont font, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        return cell.add(paragraph);
    }
    
    private Cell createHeaderCell(String content, PdfFont font) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content).setFont(font).setTextAlignment(TextAlignment.CENTER));
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        return cell;
    }
    
    private Cell createCellWithBorderRight(String content, PdfFont font, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        cell.setBorderRight(new SolidBorder(1));
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        return cell.add(paragraph);
    }
    
    private Cell createCellWithoutBorderLeft(String content, PdfFont font, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        return cell.add(paragraph);
    }

    // Kiểm tra tình trạng sẵn sàng của font
    public boolean checkFontAvailability() {
        try {
            // Tạo một văn bản mẫu để kiểm tra
            PdfFont font = getFont(false);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Thêm một đoạn văn bản tiếng Việt để kiểm tra
            document.add(new Paragraph("Tiếng Việt Kiểm tra UTF-8 - ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝĂĐĨŨƠƯ").setFont(font));
            document.close();
            
            log.info("Kiểm tra font thành công - hỗ trợ tiếng Việt");
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi kiểm tra font: {}", e.getMessage(), e);
            return false;
        }
    }
    
    // Lấy thông tin về font đã đăng ký
    public Map<String, Object> getFontInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("fontCacheSize", fontCache.size());
        info.put("registeredFonts", fontCache.keySet());
        
        try {
            // Thêm thông tin về font trong classpath
            ClassPathResource resource = new ClassPathResource("fonts/times.ttf");
            info.put("fontExists", resource.exists());
            if (resource.exists()) {
                info.put("fontPath", resource.getURL().toString());
                info.put("fontSize", resource.contentLength());
            }
        } catch (Exception e) {
            info.put("fontError", e.getMessage());
        }
        
        return info;
    }
} 