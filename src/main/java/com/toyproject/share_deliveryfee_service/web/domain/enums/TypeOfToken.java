package com.toyproject.share_deliveryfee_service.web.domain.enums;

public enum TypeOfToken {

    IAMPORT("아임포트");

    private final String description;

    TypeOfToken(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
