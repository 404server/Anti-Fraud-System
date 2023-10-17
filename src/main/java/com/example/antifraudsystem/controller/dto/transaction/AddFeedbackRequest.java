package com.example.antifraudsystem.controller.dto.transaction;

import lombok.Data;

@Data
public class AddFeedbackRequest {
    private Long transactionId;
    private String feedback;
}
