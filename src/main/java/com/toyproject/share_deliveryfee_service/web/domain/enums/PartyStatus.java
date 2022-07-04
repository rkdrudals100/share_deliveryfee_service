package com.toyproject.share_deliveryfee_service.web.domain.enums;

//  2022-04-14 강경민
//  도메인 설계
public enum PartyStatus {
    RECRUITING("모집 중"), RECCOMPLETED("모집 완료"), ORDERED("주문 완료"), DELCOMPLETED("배달 완료");

    private final String description;

    PartyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
