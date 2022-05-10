package com.toyproject.share_deliveryfee_service.web.domain;

//  2022-04-14 강경민
//  도메인 설계
public enum PaymentStatus {
    BEPAID("결제 완료"), NOTPAID("결제 X");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
