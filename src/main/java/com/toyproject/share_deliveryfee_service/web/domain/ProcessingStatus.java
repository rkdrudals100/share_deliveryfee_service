package com.toyproject.share_deliveryfee_service.web.domain;

public enum ProcessingStatus {
    PROCESSED("처리 완료"), NOTYET("처리 중");

    private final String description;

    ProcessingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
