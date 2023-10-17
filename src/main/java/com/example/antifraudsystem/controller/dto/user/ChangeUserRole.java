package com.example.antifraudsystem.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeUserRole {
    private String username;
    private String role;
}
