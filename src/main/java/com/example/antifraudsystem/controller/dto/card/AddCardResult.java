package com.example.antifraudsystem.controller.dto.card;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddCardResult {
    private Long id;
    private String number;
}
