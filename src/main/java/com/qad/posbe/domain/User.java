package com.qad.posbe.domain;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.constant.GenderEnum;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Size(min = 2, message = "Tên phải có ít nhất 2 ký tự")
    String name;

    @NotNull
    @Size(min = 3, message = "Tên đăng nhập phải có ít nhất 3 ký tự")
    String username;

    @NotNull
    @Size(min = 3, message = "Password phải có ít nhất 3 ký tự")
    String password;
    
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    String phoneNumber;
    
    @Email(message = "Email không hợp lệ")
    String email;
    
    @Enumerated(EnumType.STRING)
    GenderEnum gender;
    String address;
    String avatar;
    @Column(columnDefinition = "MEDIUMTEXT")
    String refreshToken;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ImportHistory> importHistories;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }
}
