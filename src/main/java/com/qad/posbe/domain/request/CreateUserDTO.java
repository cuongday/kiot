package com.qad.posbe.domain.request;

import com.qad.posbe.util.constant.GenderEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CreateUserDTO {
    @Size(min = 3, message = "Tên phải có ít nhất 3 ký tự")
    private String name;

    @NotNull
    private String username;
    
    @NotNull
    private String password;
    
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    private String phoneNumber;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private GenderEnum gender;
    private String address;
    private Long roleId;
}
