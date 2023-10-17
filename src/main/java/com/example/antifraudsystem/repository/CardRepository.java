package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.repository.domain.CardModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<CardModel, Long> {
    boolean existsByCardNumber(String cardNumber);

    CardModel findByCardNumber(String cardNumber);

    List<CardModel> findByOrderByIdAsc();
}
