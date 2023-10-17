package com.example.antifraudsystem.controller.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangeUserRoleResult {
    private Long id;
    private String name;
    private String username;
    private String role;
}
