package com.example.antifraudsystem.controller.dto.transaction;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Transaction {
    private Long transactionId;
    private Long amount;
    private String ip;
    private String number;
    private String region;
    private String date;
    private String result;
    private String feedback;
}
