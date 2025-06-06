package com.qad.posbe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.constant.ProductStatus;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Size(min = 2, message = "Tên phải có ít nhất 2 ký tự")
    String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    // Giá mua sản phẩm lớn hơn hoặc bằng 0
    @Min(value = 0, message = "Giá mua sản phẩm phải lớn hơn hoặc bằng 0")
    long buyPrice;

    // Giá bán sản phẩm lớn hơn hoặc bằng 0
    @Min(value = 0, message = "Giá bán sản phẩm phải lớn hơn hoặc bằng 0")
    long sellPrice;


    int quantity;
    String image;
    
    @Enumerated(EnumType.STRING)
    ProductStatus status;
    
    Instant date;

    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;


    @ManyToOne
    @JoinColumn(name = "supplier_id")
    Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ImportDetail> importDetails;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        
        // Set default status if not set
        if (this.status == null) {
            this.status = this.quantity <= 0 ? ProductStatus.OUT_OF_STOCK : ProductStatus.IN_STOCK;
        }
        
        // Auto update status based on quantity
        updateStatusBasedOnQuantity();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        
        // Auto update status based on quantity
        updateStatusBasedOnQuantity();
    }
    
    /**
     * Tự động cập nhật trạng thái sản phẩm dựa trên số lượng
     * Khi quantity = 0 và status là DANG_BAN thì tự động chuyển thành HET_HANG
     * Khi quantity > 0 và status là HET_HANG thì tự động chuyển thành DANG_BAN
     */
    private void updateStatusBasedOnQuantity() {
        if (this.quantity <= 0 && this.status == ProductStatus.IN_STOCK) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else if (this.quantity > 0 && this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.IN_STOCK;
        }
        // Không tự động thay đổi từ NGUNG_KINH_DOANH
    }
    
    /**
     * Phương thức setter custom cho quantity để tự động cập nhật status
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (this.status != null) {
            updateStatusBasedOnQuantity();
        }
    }
}
