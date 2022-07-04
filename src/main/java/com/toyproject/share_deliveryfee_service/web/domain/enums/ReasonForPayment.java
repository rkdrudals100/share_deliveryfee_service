package com.toyproject.share_deliveryfee_service.web.domain.enums;

public enum ReasonForPayment {
    JOINPARTY("파티 참가 신청");

    private final String description;

    ReasonForPayment(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
