package com.toyproject.share_deliveryfee_service.web.domain;

//  2022-04-14 강경민
//  도메인 설계
public enum FoodReceive {
    RECEIVED("음식 수령 완료"), DIDNTRECEIVED("음식 수령 X");

    private final String description;

    FoodReceive(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
