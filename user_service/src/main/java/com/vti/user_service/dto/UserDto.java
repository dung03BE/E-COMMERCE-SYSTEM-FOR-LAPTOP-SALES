package com.vti.user_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UserDto {
    private String fullName;
    private String phoneNumber;
    private String password;
}
