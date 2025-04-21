package com.qad.posbe.service;

import com.qad.posbe.domain.Product;
import com.qad.posbe.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelExportService {

    private final ProductRepository productRepository;
    
    public ByteArrayInputStream exportProductsToExcel() throws IOException {
        List<Product> products = productRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Danh sách sản phẩm");
            
            // Tạo font và style cho header
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Tạo header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Tên sản phẩm", "Nhà cung cấp", "Giá mua", "Giá bán", "Số lượng", "Danh mục", "Trạng thái"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Định dạng ngày tháng
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            // Điền dữ liệu sản phẩm
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getSupplier() != null ? product.getSupplier().getName() : "");
                row.createCell(3).setCellValue(product.getBuyPrice());
                row.createCell(4).setCellValue(product.getSellPrice());
                row.createCell(5).setCellValue(product.getQuantity());
                row.createCell(6).setCellValue(product.getCategory() != null ? product.getCategory().getName() : "");
                row.createCell(7).setCellValue(product.getStatus() != null ? product.getStatus() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
} 