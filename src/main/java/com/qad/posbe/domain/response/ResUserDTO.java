package com.qad.posbe.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import com.qad.posbe.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResUserDTO {
    Long id;
    String username;
    String name;
    String phoneNumber;
    String email;
    GenderEnum gender;
    String address;
    Instant updatedAt;  
    Instant createdAt;
    RoleUser role;
    String avatar;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleUser{
        Long id;
        String name;
    }
}
