package com.example.antifraudsystem.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeAccountState {
    private String username;
    private String operation;
}
