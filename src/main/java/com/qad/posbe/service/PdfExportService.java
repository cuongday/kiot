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
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public byte[] generateInvoicePdf(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
            
            List<OrderDetail> orderDetails = orderService.getOrderDetails(orderId);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            
            // Sử dụng khổ giấy A5
            Document document = new Document(pdfDoc, PageSize.A5);
            
            // Thiết lập font chữ có hỗ trợ tiếng Việt
            PdfFont font;
            try {
                font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (Exception e) {
                font = PdfFontFactory.createFont();
            }
            
            // Tạo tiêu đề hóa đơn
            Paragraph header = new Paragraph("CỬA HÀNG TẠP HÓA THÁI THỤY")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setBold()
                    .setFontSize(16);
            document.add(header);
            
            // Thêm địa chỉ và thông tin liên hệ
            Paragraph address = new Paragraph("Địa chỉ: 123 Đường ABC, Quận XYZ, TP.HCM")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontSize(10);
            document.add(address);
            
            Paragraph contact = new Paragraph("SĐT: 0123456789 - Email: taphothaitthuy@example.com")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontSize(10);
            document.add(contact);
            
            // Tạo tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(20);
            document.add(title);
            
            // Thêm thông tin hóa đơn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date orderDate = Date.from(order.getCreatedAt());
            
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            // Thêm số hóa đơn
            infoTable.addCell(createCell("Số hóa đơn:", font, true, TextAlignment.LEFT));
            infoTable.addCell(createCell("#" + order.getId(), font, false, TextAlignment.RIGHT));
            
            // Thêm ngày tạo hóa đơn
            infoTable.addCell(createCell("Ngày:", font, true, TextAlignment.LEFT));
            infoTable.addCell(createCell(dateFormat.format(orderDate), font, false, TextAlignment.RIGHT));
            
            // Thêm thông tin khách hàng
            if (order.getCustomer() != null) {
                infoTable.addCell(createCell("Khách hàng:", font, true, TextAlignment.LEFT));
                infoTable.addCell(createCell(order.getCustomer().getFullname(), font, false, TextAlignment.RIGHT));
                
                infoTable.addCell(createCell("Số điện thoại:", font, true, TextAlignment.LEFT));
                infoTable.addCell(createCell(order.getCustomer().getPhone(), font, false, TextAlignment.RIGHT));
            } else {
                infoTable.addCell(createCell("Khách hàng:", font, true, TextAlignment.LEFT));
                infoTable.addCell(createCell("Khách lẻ", font, false, TextAlignment.RIGHT));
            }
            
            // Thêm thông tin người bán
            infoTable.addCell(createCell("Nhân viên:", font, true, TextAlignment.LEFT));
            infoTable.addCell(createCell(order.getUser().getName(), font, false, TextAlignment.RIGHT));
            
            document.add(infoTable);
            
            // Thêm bảng danh sách sản phẩm
            Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 40, 15, 20, 20}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(15);
            
            // Tạo header cho bảng sản phẩm
            productTable.addCell(createHeaderCell("STT", font));
            productTable.addCell(createHeaderCell("Sản phẩm", font));
            productTable.addCell(createHeaderCell("SL", font));
            productTable.addCell(createHeaderCell("Đơn giá", font));
            productTable.addCell(createHeaderCell("Thành tiền", font));
            
            // Thêm các sản phẩm vào bảng
            int index = 1;
            DecimalFormat priceFormat = new DecimalFormat("#,###");
            
            for (OrderDetail detail : orderDetails) {
                productTable.addCell(createCell(String.valueOf(index++), font, false, TextAlignment.CENTER));
                productTable.addCell(createCell(detail.getProduct().getName(), font, false, TextAlignment.LEFT));
                productTable.addCell(createCell(String.valueOf(detail.getQuantity()), font, false, TextAlignment.CENTER));
                productTable.addCell(createCell(priceFormat.format(detail.getPrice()) + "đ", font, false, TextAlignment.RIGHT));
                productTable.addCell(createCell(priceFormat.format(detail.getTotalPrice()) + "đ", font, false, TextAlignment.RIGHT));
            }
            
            document.add(productTable);
            
            // Thêm tổng tiền
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            summaryTable.addCell(createCellWithBorderRight("Tổng cộng:", font, true, TextAlignment.RIGHT));
            summaryTable.addCell(createCellWithoutBorderLeft(priceFormat.format(order.getTotalPrice()) + "đ", font, true, TextAlignment.RIGHT));
            
            document.add(summaryTable);
            
            // Thêm thông tin thanh toán
            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginTop(10);
            
            paymentTable.addCell(createCell("Phương thức thanh toán:", font, true, TextAlignment.LEFT));
            String paymentMethod = order.getPaymentMethod() != null ? 
                    (order.getPaymentMethod().toString().equals("CASH") ? "Tiền mặt" : "Chuyển khoản") : "Tiền mặt";
            paymentTable.addCell(createCell(paymentMethod, font, false, TextAlignment.RIGHT));
            
            paymentTable.addCell(createCell("Trạng thái thanh toán:", font, true, TextAlignment.LEFT));
            String paymentStatus = order.getPaymentStatus() != null ? 
                    (order.getPaymentStatus().toString().equals("PAID") ? "Đã thanh toán" : "Chưa thanh toán") : "Chưa thanh toán";
            paymentTable.addCell(createCell(paymentStatus, font, false, TextAlignment.RIGHT));
            
            document.add(paymentTable);
            
            // Thêm lời cảm ơn
            Paragraph thankYou = new Paragraph("Cảm ơn Quý khách đã mua hàng!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(font)
                    .setFontSize(12)
                    .setMarginTop(20);
            document.add(thankYou);
            
            // Đóng tài liệu
            document.close();
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo hóa đơn PDF: " + e.getMessage(), e);
        }
    }
    
    private Cell createCell(String content, PdfFont font, boolean isBold, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        
        if (isBold) {
            paragraph.setBold();
        }
        
        return cell.add(paragraph);
    }
    
    private Cell createHeaderCell(String content, PdfFont font) {
        Cell cell = new Cell();
        cell.add(new Paragraph(content).setFont(font).setBold().setTextAlignment(TextAlignment.CENTER));
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        return cell;
    }
    
    private Cell createCellWithBorderRight(String content, PdfFont font, boolean isBold, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        cell.setBorderRight(new SolidBorder(1));
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        
        if (isBold) {
            paragraph.setBold();
        }
        
        return cell.add(paragraph);
    }
    
    private Cell createCellWithoutBorderLeft(String content, PdfFont font, boolean isBold, TextAlignment alignment) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        Paragraph paragraph = new Paragraph(content).setFont(font).setTextAlignment(alignment);
        
        if (isBold) {
            paragraph.setBold();
        }
        
        return cell.add(paragraph);
    }
} 