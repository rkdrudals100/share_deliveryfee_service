package com.toyproject.share_deliveryfee_service.web.domain;

//  2022-04-14 강경민
//  도메인 설계
public enum DeliveryPlatform {

    BM("배달의 민족"), BT("배달특급"), YGY("요기요");

    private final String name;

    DeliveryPlatform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
