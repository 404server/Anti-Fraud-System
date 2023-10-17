package com.example.antifraudsystem.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistration {
    private String name;
    private String username;
    private String password;
}
