package com.example.antifraudsystem.service.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeAccountStateDto {
    private ChangeStateResult status;
}
