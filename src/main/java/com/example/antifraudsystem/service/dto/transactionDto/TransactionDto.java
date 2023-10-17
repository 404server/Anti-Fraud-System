package com.example.antifraudsystem.service.dto.transactionDto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
public class TransactionDto {
    private Long transactionId;
    private Long amount;
    private String ip;
    private String number;
    private String region;
    private Timestamp date;
    private String result;
    private String feedback;
}
