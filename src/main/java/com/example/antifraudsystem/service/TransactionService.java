package com.example.antifraudsystem.service;

import com.example.antifraudsystem.repository.CardRepository;
import com.example.antifraudsystem.repository.IpRepository;
import com.example.antifraudsystem.repository.LimitRepository;
import com.example.antifraudsystem.repository.TransactionRepository;
import com.example.antifraudsystem.repository.domain.Info;
import com.example.antifraudsystem.repository.domain.LimitModel;
import com.example.antifraudsystem.repository.domain.TransactionModel;
import com.example.antifraudsystem.service.dto.transactionDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private IpRepository ipRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private LimitRepository limitRepository;

    private final static long MAX_REGION_COUNT = 2;
    private final static long MAX_IP_COUNT = 2;

    private final static long LIMIT_ID = 1L;

    public TransactionResultDto makeTransaction(TransactionDto transactionDto) {
        TransactionResultDto resultDto = new TransactionResultDto();
        List<String> reasons = new ArrayList<>();
        TransactionResult result = getTransactionResult(transactionDto.getAmount());
        resultDto.setTransactionResult(result);

        LocalDateTime requestDate = transactionDto.getDate().toLocalDateTime();
        LocalDateTime hourBeforeRequestDate = requestDate.minusHours(1);
        Timestamp hourBeforeRequestTimestamp = Timestamp.valueOf(hourBeforeRequestDate);

        long distinctRegionCount = transactionRepository.getDistinctRegion(transactionDto.getRegion(),
                transactionDto.getNumber(),
                hourBeforeRequestTimestamp, transactionDto.getDate());

        long distinctIpCount = transactionRepository.getDistinctIp(transactionDto.getIp(),
                transactionDto.getNumber(),
                hourBeforeRequestTimestamp, transactionDto.getDate());


        if (result != TransactionResult.ALLOWED) {
            reasons.add(Info.AMOUNT.getValue());
        }
        if (reasons.contains(Info.AMOUNT.getValue()) && ((cardRepository.existsByCardNumber(transactionDto.getNumber())
                || ipRepository.existsByIp(transactionDto.getIp())) && result == TransactionResult.MANUAL_PROCESSING)) {
            reasons.remove(Info.AMOUNT.getValue());
        }

        if (cardRepository.existsByCardNumber(transactionDto.getNumber())) {
            reasons.add(Info.CARD_NUMBER.getValue());
            resultDto.setTransactionResult(TransactionResult.PROHIBITED);
        }
        if (ipRepository.existsByIp(transactionDto.getIp())) {
            reasons.add(Info.IP.getValue());
            resultDto.setTransactionResult(TransactionResult.PROHIBITED);
        }

        if (distinctIpCount > MAX_IP_COUNT) {
            reasons.add(Info.IP_CORRELATION.getValue());
            resultDto.setTransactionResult(TransactionResult.PROHIBITED);
        }
        if (distinctIpCount == MAX_IP_COUNT) {
            reasons.add(Info.IP_CORRELATION.getValue());
            resultDto.setTransactionResult(TransactionResult.MANUAL_PROCESSING);
        }

        if (distinctRegionCount > MAX_REGION_COUNT) {
            reasons.add(Info.REGION_CORRELATION.getValue());
            resultDto.setTransactionResult(TransactionResult.PROHIBITED);
        }
        if (distinctRegionCount == MAX_REGION_COUNT) {
            reasons.add(Info.REGION_CORRELATION.getValue());
            resultDto.setTransactionResult(TransactionResult.MANUAL_PROCESSING);
        }

        if (reasons.isEmpty()) {
            reasons.add(Info.NONE.getValue());
        }
        resultDto.setInfo(reasons);

        TransactionModel transactionModel = new TransactionModel(transactionDto.getAmount(),
                transactionDto.getIp(), transactionDto.getNumber(), transactionDto.getRegion(),
                transactionDto.getDate(), resultDto.getTransactionResult().name());
        transactionRepository.save(transactionModel);


        return resultDto;
    }


    public AddFeedbackResultDto addFeedback(FeedbackDto feedbackDto) {
        if (!transactionRepository.existsByTransactionId(feedbackDto.getTransactionId())) {
            return new AddFeedbackResultDto(AddFeedbackResult.NOT_FOUND);
        }
        TransactionModel transactionModel = transactionRepository.getModelByID(feedbackDto.getTransactionId());
        if (!transactionModel.getFeedback().isEmpty()) {
            return new AddFeedbackResultDto(AddFeedbackResult.CONFLICT);
        }
        if (transactionModel.getResult().equals(feedbackDto.getFeedback())) {
            return new AddFeedbackResultDto(AddFeedbackResult.UNPROCESSABLE);
        }

        LimitModel limitModel = limitRepository.getLimitModelById(LIMIT_ID);
        long maxManual = limitModel.getMaxManual();
        long maxAllowed = limitModel.getMaxAllowed();
        if (feedbackDto.getFeedback().equals("ALLOWED")) {
            if (transactionModel.getResult().equals(TransactionResult.MANUAL_PROCESSING.name())) {
                maxAllowed = (long) Math.ceil(0.8 * maxAllowed + 0.2 * transactionModel.getAmount());
            }
            if (transactionModel.getResult().equals(TransactionResult.PROHIBITED.name())) {
                maxAllowed = (long) Math.ceil(0.8 * maxAllowed + 0.2 * transactionModel.getAmount());
                maxManual = (long) Math.ceil(0.8 * maxManual + 0.2 * transactionModel.getAmount());
            }
        }
        if (feedbackDto.getFeedback().equals("MANUAL_PROCESSING")) {
            if (transactionModel.getResult().equals(TransactionResult.ALLOWED.name())) {
                maxAllowed = (long) Math.ceil(0.8 * maxAllowed - 0.2 * transactionModel.getAmount());
            }
            if (transactionModel.getResult().equals(TransactionResult.PROHIBITED.name())) {
                maxManual = (long) Math.ceil(0.8 * maxManual + 0.2 * transactionModel.getAmount());
            }

        }
        if (feedbackDto.getFeedback().equals("PROHIBITED")) {
            if (transactionModel.getResult().equals(TransactionResult.ALLOWED.name())) {
                maxAllowed = (long) Math.ceil(0.8 * maxAllowed - 0.2 * transactionModel.getAmount());
                maxManual = (long) Math.ceil(0.8 * maxManual - 0.2 * transactionModel.getAmount());
            }
            if (transactionModel.getResult().equals(TransactionResult.MANUAL_PROCESSING.name())) {
                maxManual = (long) Math.ceil(0.8 * maxManual - 0.2 * transactionModel.getAmount());
            }
        }
        limitModel.setMaxAllowed(maxAllowed);
        limitModel.setMaxManual(maxManual);
        limitRepository.save(limitModel);

        transactionModel.setFeedback(feedbackDto.getFeedback());
        transactionRepository.save(transactionModel);
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(transactionModel.getTransactionId())
                .setAmount(transactionModel.getAmount())
                .setIp(transactionModel.getIp())
                .setNumber(transactionModel.getNumber())
                .setRegion(transactionModel.getRegion())
                .setDate(transactionModel.getDate())
                .setResult(transactionModel.getResult())
                .setFeedback(transactionModel.getFeedback());
        return new AddFeedbackResultDto(transactionDto, AddFeedbackResult.SUCCESS);
    }

    public List<TransactionDto> getHistory() {
        List<TransactionDto> history = new ArrayList<>();
        for (TransactionModel transactionModel : transactionRepository.findByOrderByTransactionIdAsc()) {
            history.add(new TransactionDto()
                    .setTransactionId((transactionModel.getTransactionId()))
                    .setAmount(transactionModel.getAmount())
                    .setIp(transactionModel.getIp())
                    .setNumber(transactionModel.getNumber())
                    .setRegion(transactionModel.getRegion())
                    .setDate(transactionModel.getDate())
                    .setResult(transactionModel.getResult())
                    .setFeedback(transactionModel.getFeedback()));
        }
        return history;
    }


    public List<TransactionDto> getHistoryByNumber(String number) {
        List<TransactionModel> transactionModels = transactionRepository.getTransactionsByNumber(number);
        List<TransactionDto> history = new ArrayList<>();
        for (TransactionModel transactionModel : transactionModels) {
            history.add(new TransactionDto()
                    .setTransactionId((transactionModel.getTransactionId()))
                    .setAmount(transactionModel.getAmount())
                    .setIp(transactionModel.getIp())
                    .setNumber(transactionModel.getNumber())
                    .setRegion(transactionModel.getRegion())
                    .setDate(transactionModel.getDate())
                    .setResult(transactionModel.getResult())
                    .setFeedback(transactionModel.getFeedback()));
        }
        return history;
    }


    public TransactionResult getTransactionResult(Long amount) {
        LimitModel limitModel = limitRepository.getLimitModelById(LIMIT_ID);
        long maxManual = limitModel.getMaxManual();
        long maxAllowed = limitModel.getMaxAllowed();
        if (amount <= maxAllowed) {
            return TransactionResult.ALLOWED;
        }
        if (amount <= maxManual) {
            return TransactionResult.MANUAL_PROCESSING;
        }
        return TransactionResult.PROHIBITED;
    }

}
