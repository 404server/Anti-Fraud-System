package com.example.antifraudsystem.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transaction_history")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
    @Column(name = "transaction_id")
    private Long transactionId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "ip")
    private String ip;
    @Column(name = "number")
    private String number;
    @Column(name = "region")
    private String region;
    @Column(name = "date")
    private Timestamp date;
    @Column(name = "result")
    private String result;
    @Column(name = "feedback")
    private String feedback = "";

    public TransactionModel() {
    }

    public TransactionModel(Long amount, String ip, String number, String region,
                            Timestamp date, String result) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.result = result;
    }
}