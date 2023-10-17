package com.example.antifraudsystem.service.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeUserRoleDto {
    private UserDto userDto;
    private ChangeRoleResult result;

    public ChangeUserRoleDto(ChangeRoleResult result) {
        this.result = result;
    }
}
