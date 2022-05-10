package com.toyproject.share_deliveryfee_service.web.domain;

//  2022-04-14 강경민
//  도메인 설계
public enum ReadStatus {
    READ("확인 완료"), NOTREAD("아직 확인 X");

    private final String description;

    ReadStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
