package com.example.antifraudsystem.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "credit_cards")
public class CardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @SequenceGenerator(name = "card_seq", sequenceName = "card_seq", allocationSize = 1)
    private Long id;
    @Column(name = "card_number")
    private String cardNumber;

    public CardModel() {
    }

    public CardModel(String cardNumber) {
        this.cardNumber = cardNumber;
    }

}
