package com.example.antifraudsystem.service.dto.cardDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardAddResultDto {
    private CardDto cardDto;
    private boolean success;

    public CardAddResultDto(boolean success) {
        this.success = success;
    }
}
