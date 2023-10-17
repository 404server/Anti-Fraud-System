package com.example.antifraudsystem.service.dto.transactionDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeedbackDto {
    private Long transactionId;
    private String feedback;
}
