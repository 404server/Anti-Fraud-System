package com.example.antifraudsystem.service.dto.userDto;

import com.example.antifraudsystem.repository.domain.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Role role;
}
