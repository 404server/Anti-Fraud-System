package com.example.antifraudsystem.service.dto.cardDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardDto {
    private Long id;
    private String number;
}
