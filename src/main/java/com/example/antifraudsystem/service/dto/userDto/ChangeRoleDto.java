package com.example.antifraudsystem.service.dto.userDto;

import com.example.antifraudsystem.repository.domain.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangeRoleDto {
    private String username;
    private Role role;
}
