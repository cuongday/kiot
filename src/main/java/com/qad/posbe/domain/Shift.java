package com.qad.posbe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.constant.ShiftStatus;

import java.time.Instant;

@Entity
@Table(name = "shifts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    Instant startTime;
    
    Instant endTime;
    
    @Min(value = 0, message = "Số tiền đầu ca phải lớn hơn hoặc bằng 0")
    long startCash;
    
    @Min(value = 0, message = "Số tiền cuối ca phải lớn hơn hoặc bằng 0")
    long endCash;
    
    @Min(value = 0, message = "Số tiền dự kiến phải lớn hơn hoặc bằng 0")
    long expectedCash;
    
    @Enumerated(EnumType.STRING)
    ShiftStatus status;
    
    @Column(columnDefinition = "TEXT")
    String note;
    
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }
} 