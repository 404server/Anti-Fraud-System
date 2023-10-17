package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.controller.dto.transaction.AddFeedbackRequest;
import com.example.antifraudsystem.controller.dto.transaction.AddTransactionRequest;
import com.example.antifraudsystem.controller.dto.transaction.Transaction;
import com.example.antifraudsystem.controller.dto.transaction.TransactionResult;
import com.example.antifraudsystem.service.TransactionService;
import com.example.antifraudsystem.service.dto.transactionDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.antifraudsystem.controller.utility.Constants.DATE_PATTERN;
import static com.example.antifraudsystem.controller.utility.Constants.IP_PATTERN;
import static com.example.antifraudsystem.controller.utility.Converter.convertStringToTimestamp;
import static com.example.antifraudsystem.controller.utility.TransactionValidator.isCardNumberValid;
import static com.example.antifraudsystem.controller.utility.TransactionValidator.isRegionValid;

@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResult> postTransaction(@RequestBody AddTransactionRequest transaction) {
        if (transaction == null
                || transaction.getAmount() == null
                || transaction.getAmount() <= 0
                || !transaction.getIp().matches(IP_PATTERN)
                || !isCardNumberValid(transaction.getNumber())
                || transaction.getRegion() == null
                || !isRegionValid(transaction.getRegion())
                || transaction.getDate() == null
                || !transaction.getDate().matches(DATE_PATTERN)) {
            return ResponseEntity.badRequest().build();
        }
        TransactionDto transactionDto = convertTransactionDto(transaction);
        TransactionResultDto resultDto = transactionService.makeTransaction(transactionDto);
        String info = String.join(", ", resultDto.getInfo());

        return new ResponseEntity<>(
                new TransactionResult(resultDto.getTransactionResult().name(),
                        info), HttpStatus.OK);
    }

    @PutMapping("/transaction")
    public ResponseEntity<Transaction> addFeedback(@RequestBody AddFeedbackRequest feedbackRequest) {
        if (feedbackRequest == null || feedbackRequest.getTransactionId() == null
                || feedbackRequest.getFeedback() == null || !validateFeedback(feedbackRequest.getFeedback())) {
            return ResponseEntity.badRequest().build();
        }
        FeedbackDto feedbackDto = convertFeedbackDto(feedbackRequest);
        AddFeedbackResultDto feedbackResultDto = transactionService.addFeedback(feedbackDto);

        if (feedbackResultDto.getResult().equals(AddFeedbackResult.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (feedbackResultDto.getResult().equals(AddFeedbackResult.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (feedbackResultDto.getResult().equals(AddFeedbackResult.UNPROCESSABLE)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        return new ResponseEntity<>(convertFeedbackResult(feedbackResultDto.getTransactionDto()), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory() {
        return new ResponseEntity<>(convertTransactionList(transactionService.getHistory()), HttpStatus.OK);
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<List<Transaction>> getTransactionHistoryByNumber(@PathVariable String number) {
        if (!isCardNumberValid(number)) {
            return ResponseEntity.badRequest().build();
        }
        List<Transaction> transactionsByNumber = convertTransactionList(transactionService.getHistoryByNumber(number));
        if (transactionsByNumber.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(transactionsByNumber, HttpStatus.OK);
    }

    public TransactionDto convertTransactionDto(AddTransactionRequest transaction) {
        Timestamp timestamp = convertStringToTimestamp(transaction.getDate());
        return new TransactionDto()
                .setAmount(transaction.getAmount())
                .setIp(transaction.getIp())
                .setNumber(transaction.getNumber())
                .setRegion(transaction.getRegion())
                .setDate(timestamp);
    }

    public FeedbackDto convertFeedbackDto(AddFeedbackRequest feedbackRequest) {
        return new FeedbackDto()
                .setTransactionId(feedbackRequest.getTransactionId())
                .setFeedback(feedbackRequest.getFeedback());
    }

    public boolean validateFeedback(String feedback) {
        List<String> possibleFeedbacks = List.of("ALLOWED", "MANUAL_PROCESSING", "PROHIBITED");
        return possibleFeedbacks.contains(feedback);
    }

    public static Transaction convertFeedbackResult(TransactionDto transactionDto) {
        return new Transaction()
                .setTransactionId(transactionDto.getTransactionId())
                .setAmount(transactionDto.getAmount())
                .setIp(transactionDto.getIp())
                .setNumber(transactionDto.getNumber())
                .setRegion(transactionDto.getRegion())
                .setDate(convertTimestampToString(transactionDto.getDate()))
                .setResult(transactionDto.getResult())
                .setFeedback(transactionDto.getFeedback());
    }

    public static List<Transaction> convertTransactionList(List<TransactionDto> transactionDtoList) {
        List<Transaction> history = new ArrayList<>();

        for (TransactionDto transactionDto : transactionDtoList) {
            history.add(new Transaction()
                    .setTransactionId(transactionDto.getTransactionId())
                    .setAmount(transactionDto.getAmount())
                    .setIp(transactionDto.getIp())
                    .setNumber(transactionDto.getNumber())
                    .setRegion(transactionDto.getRegion())
                    .setDate(convertTimestampToString(transactionDto.getDate()))
                    .setResult(transactionDto.getResult())
                    .setFeedback(transactionDto.getFeedback()));
        }

        return history;
    }

    public static String convertTimestampToString(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return localDateTime.format(formatter);
    }


}