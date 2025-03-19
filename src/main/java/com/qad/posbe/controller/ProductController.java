package com.qad.posbe.controller;

import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.mapper.ProductMapper;
import com.qad.posbe.domain.request.CreateProductDTO;
import com.qad.posbe.domain.request.UpdateProductDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.service.ProductService;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    // Lấy tất cả sản phẩm với phân trang
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/products")
    @ApiMessage("Lấy tất cả sản phẩm")
    public ResponseEntity<ResultPaginationDTO> getProducts(
            @Filter Specification<Product> productSpec,
            Pageable pageable
    ) {
        ResultPaginationDTO rs = this.productService.getAll(productSpec, pageable);
        return ResponseEntity.ok(rs);
    }

    // Lấy chi tiết sản phẩm theo ID
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/products/{id}")
    @ApiMessage("Lấy sản phẩm theo id")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) throws IdInvalidException {
        Product product = this.productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // Tạo sản phẩm mới
    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Tạo sản phẩm mới")
    public ResponseEntity<Product> createProduct(
            @Valid @ModelAttribute CreateProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        boolean isNameExist = this.productService.existsByName(productDTO.getName());
        if(isNameExist) {
            throw new IdInvalidException("Tên sản phẩm đã tồn tại");
        }
        
        Product product = this.productMapper.toEntity(productDTO);
        
        // Thiết lập Category và Supplier ID
        product.setCategory(new com.qad.posbe.domain.Category());
        product.getCategory().setId(productDTO.getCategoryId());
        
        product.setSupplier(new com.qad.posbe.domain.Supplier());
        product.getSupplier().setId(productDTO.getSupplierId());
        
        Product newProduct = this.productService.handleCreateProduct(product, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // Cập nhật sản phẩm
    @PreAuthorize("hasRole('admin')")
    @PutMapping(value = "/products/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Cập nhật sản phẩm theo id")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute UpdateProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        // Kiểm tra xem sản phẩm có tồn tại không
        this.productService.getById(id);
        
        // Đảm bảo ID trong DTO khớp với ID trong đường dẫn
        productDTO.setId(id);
        
        // Kiểm tra nếu thay đổi tên, đảm bảo tên mới không trùng với sản phẩm khác
        Product existingProduct = this.productService.getById(id);
        if (!existingProduct.getName().equals(productDTO.getName()) && this.productService.existsByName(productDTO.getName())) {
            throw new IdInvalidException("Sản phẩm với tên này đã tồn tại");
        }
        
        this.productMapper.updateEntityFromDto(productDTO, existingProduct);
        
        // Thiết lập Category và Supplier ID
        existingProduct.setCategory(new com.qad.posbe.domain.Category());
        existingProduct.getCategory().setId(productDTO.getCategoryId());
        
        existingProduct.setSupplier(new com.qad.posbe.domain.Supplier());
        existingProduct.getSupplier().setId(productDTO.getSupplierId());
        
        Product updatedProduct = this.productService.handleUpdateProduct(id, existingProduct, imageFile);
        return ResponseEntity.ok(updatedProduct);
    }

    // Xóa sản phẩm
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/products/{id}")
    @ApiMessage("Xóa sản phẩm theo id")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) throws IdInvalidException {
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Lấy sản phẩm theo danh mục
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/products/category/{categoryId}")
    @ApiMessage("Lấy sản phẩm theo danh mục")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) throws IdInvalidException {
        return ResponseEntity.ok(this.productService.getByCategory(categoryId));
    }

    // Lấy sản phẩm theo nhà cung cấp
    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/products/supplier/{supplierId}")
    @ApiMessage("Lấy sản phẩm theo nhà cung cấp")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable("supplierId") Long supplierId) throws IdInvalidException {
        return ResponseEntity.ok(productService.getBySupplier(supplierId));
    }
} 