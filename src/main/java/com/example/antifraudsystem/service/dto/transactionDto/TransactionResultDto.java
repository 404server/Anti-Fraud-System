package com.example.antifraudsystem.service.dto.transactionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResultDto {
    private TransactionResult transactionResult;
    private List<String> info;
}
