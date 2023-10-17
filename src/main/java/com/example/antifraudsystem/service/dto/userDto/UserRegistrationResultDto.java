package com.example.antifraudsystem.service.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistrationResultDto {
    private UserDto userDto;
    private boolean success;

    public UserRegistrationResultDto(boolean success) {
        this.success = success;
    }
}
