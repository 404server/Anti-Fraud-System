package com.example.antifraudsystem.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistrationResult {
    private Long id;
    private String name;
    private String username;
    private String role;
}