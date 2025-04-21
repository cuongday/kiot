package com.qad.posbe.controller;

import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.mapper.ProductMapper;
import com.qad.posbe.domain.request.CreateProductDTO;
import com.qad.posbe.domain.request.UpdateProductDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.service.ProductService;
import com.qad.posbe.service.ExcelExportService;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ExcelExportService excelExportService;

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("")
    @ApiMessage("Lấy danh sách sản phẩm")
    public ResponseEntity<ResultPaginationDTO> fetchAllProduct(
            @Filter Specification<Product> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO rs = this.productService.getAll(spec, pageable);
        return ResponseEntity.ok(rs);
    }

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/{id}")
    @ApiMessage("Lấy sản phẩm theo ID")
    public ResponseEntity<Product> getProduct(
            @PathVariable("id") Long id
    ) throws IdInvalidException {
        Product product = this.productService.getById(id);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Tạo sản phẩm mới")
    public ResponseEntity<Product> create(
            @Valid @ModelAttribute CreateProductDTO createProductDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        // Kiểm tra tên trùng lặp
        boolean isNameExist = this.productService.existsByName(createProductDTO.getName());
        if(isNameExist) {
            throw new IdInvalidException("Tên sản phẩm đã tồn tại");
        }
        
        // Để service xử lý toàn bộ việc chuyển đổi DTO và lưu trữ
        Product newProduct = this.productService.handleCreateProduct(createProductDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Cập nhật sản phẩm thành công")
    public ResponseEntity<Product> update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute UpdateProductDTO updateProductDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        // Kiểm tra xem sản phẩm có tồn tại không
        this.productService.getById(id);
        
        // Đảm bảo ID trong DTO khớp với ID trong đường dẫn
        updateProductDTO.setId(id);
        
        // Kiểm tra nếu thay đổi tên, đảm bảo tên mới không trùng với sản phẩm khác
        Product existingProduct = this.productService.getById(id);
        if (!existingProduct.getName().equals(updateProductDTO.getName()) && this.productService.existsByName(updateProductDTO.getName())) {
            throw new IdInvalidException("Sản phẩm với tên này đã tồn tại");
        }
        
        this.productMapper.updateEntityFromDto(updateProductDTO, existingProduct);
        
        // Thiết lập Category và Supplier ID
        existingProduct.setCategory(new com.qad.posbe.domain.Category());
        existingProduct.getCategory().setId(updateProductDTO.getCategoryId());
        
        existingProduct.setSupplier(new com.qad.posbe.domain.Supplier());
        existingProduct.getSupplier().setId(updateProductDTO.getSupplierId());
        
        Product updatedProduct = this.productService.handleUpdateProduct(id, existingProduct, imageFile);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    @ApiMessage("Xóa sản phẩm thành công")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ) throws IdInvalidException {
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Lấy sản phẩm theo danh mục
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/category/{categoryId}")
    @ApiMessage("Lấy sản phẩm theo danh mục")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) throws IdInvalidException {
        return ResponseEntity.ok(this.productService.getByCategory(categoryId));
    }

    // Lấy sản phẩm theo nhà cung cấp
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/supplier/{supplierId}")
    @ApiMessage("Lấy sản phẩm theo nhà cung cấp")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable("supplierId") Long supplierId) throws IdInvalidException {
        return ResponseEntity.ok(productService.getBySupplier(supplierId));
    }

    // Thêm endpoint mới để xuất danh sách sản phẩm ra Excel
    @GetMapping("/export-excel")
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @ApiMessage("Xuất danh sách sản phẩm ra file Excel")
    public ResponseEntity<Resource> exportProductsToExcel() {
        try {
            ByteArrayInputStream excelData = excelExportService.exportProductsToExcel();
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx");
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(excelData));
        } catch (Exception e) {
            log.error("Lỗi khi xuất file Excel: ", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 