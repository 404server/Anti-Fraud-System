package com.example.antifraudsystem.service.dto.transactionDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddFeedbackResultDto {
    private TransactionDto transactionDto;
    private AddFeedbackResult result;

    public AddFeedbackResultDto(AddFeedbackResult result) {
        this.result = result;
    }
}
