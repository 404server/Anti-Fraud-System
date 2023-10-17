package com.example.antifraudsystem.service;

import com.example.antifraudsystem.repository.CardRepository;
import com.example.antifraudsystem.repository.domain.CardModel;
import com.example.antifraudsystem.service.dto.cardDto.CardAddResultDto;
import com.example.antifraudsystem.service.dto.cardDto.CardDeleteResultDto;
import com.example.antifraudsystem.service.dto.cardDto.CardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public CardAddResultDto addCreditCard(String number) {
        if (cardRepository.existsByCardNumber(number)) {
            return new CardAddResultDto(false);
        }
        cardRepository.save(new CardModel(number));
        CardModel cardModel = cardRepository.findByCardNumber(number);
        CardDto cardDto = new CardDto().setId(cardModel.getId()).setNumber(number);

        return new CardAddResultDto(cardDto, true);
    }

    public CardDeleteResultDto deleteCard(String number) {
        if (!cardRepository.existsByCardNumber(number)) {
            return new CardDeleteResultDto(false);
        }
        CardModel cardModel = cardRepository.findByCardNumber(number);
        cardRepository.delete(cardModel);
        return new CardDeleteResultDto(true);
    }

    public List<CardDto> getCardList() {
        List<CardDto> cardDtoList = new ArrayList<>();
        for (CardModel cardModel : cardRepository.findByOrderByIdAsc()) {
            cardDtoList.add(new CardDto().setId(cardModel.getId()).setNumber(cardModel.getCardNumber()));
        }
        return cardDtoList;
    }

}
