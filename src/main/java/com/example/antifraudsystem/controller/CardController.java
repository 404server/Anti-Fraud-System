package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.controller.dto.card.AddCardRequest;
import com.example.antifraudsystem.controller.dto.card.AddCardResult;
import com.example.antifraudsystem.controller.dto.card.Card;
import com.example.antifraudsystem.controller.dto.card.CardDeleteResult;
import com.example.antifraudsystem.service.CardService;
import com.example.antifraudsystem.service.dto.cardDto.CardAddResultDto;
import com.example.antifraudsystem.service.dto.cardDto.CardDeleteResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.antifraudsystem.controller.utility.TransactionValidator.isCardNumberValid;
import static com.example.antifraudsystem.controller.utility.Converter.convertCardAddResult;
import static com.example.antifraudsystem.controller.utility.Converter.convertCardList;

@RestController
@RequestMapping("/api/antifraud")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("/stolencard")
    public ResponseEntity<AddCardResult> addCard(@RequestBody AddCardRequest addCardBody) {
        if (addCardBody == null || !isCardNumberValid(addCardBody.getNumber())) {
            return ResponseEntity.badRequest().build();
        }

        CardAddResultDto cardAddResultDto = cardService.addCreditCard(addCardBody.getNumber());
        if (!cardAddResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(convertCardAddResult(cardAddResultDto.getCardDto()), HttpStatus.OK);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<CardDeleteResult> deleteCard(@PathVariable String number) {
        if (number == null || !isCardNumberValid(number)) {
            return ResponseEntity.badRequest().build();
        }
        CardDeleteResultDto cardDeleteResultDto = cardService.deleteCard(number);
        if (!cardDeleteResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String message = String.format("Card %s successfully removed!", number);
        return new ResponseEntity<>(new CardDeleteResult(message), HttpStatus.OK);
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<Card>> getCardList() {
        return new ResponseEntity<>(convertCardList(cardService.getCardList()), HttpStatus.OK);
    }

}
