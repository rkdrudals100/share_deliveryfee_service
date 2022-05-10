package com.toyproject.share_deliveryfee_service.web.domain;

//  2022-04-14 강경민
//  도메인 설계
public enum AgreementStatus {
    AGREE("파티 참여 허가 O"), NOTAGREE("파티 참여 허가 X");

    private final String description;

    AgreementStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
