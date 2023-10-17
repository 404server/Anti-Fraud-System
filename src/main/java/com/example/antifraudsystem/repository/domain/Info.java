package com.example.antifraudsystem.repository.domain;

import lombok.Getter;

@Getter
public enum Info {
    AMOUNT("amount"),
    CARD_NUMBER("card-number"),
    IP("ip"),
    NONE("none"),
    IP_CORRELATION("ip-correlation"),
    REGION_CORRELATION("region-correlation");
    private final String value;

    Info(String value) {
        this.value = value;
    }

}
