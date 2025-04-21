package com.qad.posbe.service;

import com.qad.posbe.domain.ImportDetail;
import com.qad.posbe.domain.ImportHistory;
import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.Supplier;
import com.qad.posbe.domain.User;
import com.qad.posbe.repository.ImportDetailRepository;
import com.qad.posbe.repository.ImportHistoryRepository;
import com.qad.posbe.repository.ProductRepository;
import com.qad.posbe.repository.SupplierRepository;
import com.qad.posbe.repository.UserRepository;
import com.qad.posbe.util.SecurityUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportExcelService {
    
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ImportHistoryRepository importHistoryRepository;
    private final ImportDetailRepository importDetailRepository;
    private final UserService userService;
    
    @Transactional
    public ImportHistory processImportExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Lấy thông tin người dùng hiện tại
            User user = userService.getCurrentUser();
            
            // Đọc dữ liệu từ file Excel để tìm sản phẩm đầu tiên và lấy supplier
            List<ImportProductInfo> productInfoList = new ArrayList<>();
            Supplier supplier = null;
            
            // Bắt đầu từ dòng 1 (bỏ qua header)
            int rowNum = 1;
            while (rowNum <= sheet.getLastRowNum()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    rowNum++;
                    continue;
                }
                
                // Đọc ID sản phẩm từ cột đầu tiên
                Cell idCell = row.getCell(0);
                if (idCell == null) {
                    rowNum++;
                    continue;
                }
                
                Long productId;
                if (idCell.getCellType() == CellType.NUMERIC) {
                    productId = (long) idCell.getNumericCellValue();
                } else {
                    try {
                        productId = Long.parseLong(idCell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        rowNum++;
                        continue;
                    }
                }
                
                // Bỏ qua dòng tổng cộng
                if (row.getCell(1) != null && 
                    row.getCell(1).toString().toLowerCase().contains("tổng")) {
                    break;
                }
                
                // Đọc số lượng sản phẩm
                Cell quantityCell = row.getCell(2);
                if (quantityCell == null) {
                    rowNum++;
                    continue;
                }
                
                int quantity = (int) quantityCell.getNumericCellValue();
                
                // Lấy thông tin sản phẩm từ database
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
                
                // Nếu chưa có supplier, lấy từ sản phẩm đầu tiên
                if (supplier == null) {
                    supplier = product.getSupplier();
                    if (supplier == null) {
                        throw new EntityNotFoundException("Sản phẩm " + product.getName() + " không có thông tin nhà cung cấp");
                    }
                }
                
                // Kiểm tra sản phẩm có cùng nhà cung cấp không
                if (!product.getSupplier().getId().equals(supplier.getId())) {
                    throw new IllegalArgumentException(
                        "Sản phẩm " + product.getName() + " thuộc nhà cung cấp khác với những sản phẩm khác trong file. " +
                        "Tất cả sản phẩm trong một lần nhập phải thuộc cùng một nhà cung cấp."
                    );
                }
                
                // Thêm thông tin sản phẩm vào danh sách
                productInfoList.add(new ImportProductInfo(product, quantity));
                
                rowNum++;
            }
            
            if (supplier == null) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm hợp lệ nào trong file Excel");
            }
            
            // Tạo đối tượng ImportHistory
            ImportHistory importHistory = ImportHistory.builder()
                .user(user)
                .supplier(supplier)
                .totalPrice(0)
                .build();
            
            importHistoryRepository.save(importHistory);
            
            // Tạo chi tiết nhập hàng từ danh sách sản phẩm đã đọc
            long totalPrice = 0;
            
            for (ImportProductInfo info : productInfoList) {
                Product product = info.getProduct();
                int quantity = info.getQuantity();
                
                // Sử dụng giá từ database
                long price = product.getBuyPrice();
                long productTotalPrice = price * quantity;
                
                // Tạo chi tiết nhập hàng
                ImportDetail importDetail = ImportDetail.builder()
                    .product(product)
                    .importHistory(importHistory)
                    .quantity(quantity)
                    .price(price)
                    .totalPrice(productTotalPrice)
                    .build();
                
                importDetailRepository.save(importDetail);
                
                // Cập nhật số lượng trong kho
                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
                
                totalPrice += productTotalPrice;
            }
            
            // Cập nhật tổng giá trị của phiếu nhập
            importHistory.setTotalPrice(totalPrice);
            return importHistoryRepository.save(importHistory);
        }
    }
    
    // Lớp nội bộ để lưu thông tin sản phẩm và số lượng
    private static class ImportProductInfo {
        private final Product product;
        private final int quantity;
        
        public ImportProductInfo(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
} 