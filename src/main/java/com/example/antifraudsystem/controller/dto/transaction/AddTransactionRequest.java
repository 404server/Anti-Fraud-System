package com.example.antifraudsystem.controller.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddTransactionRequest {
    private Long amount;
    private String ip;
    private String number;
    private String region;
    private String date;
}
