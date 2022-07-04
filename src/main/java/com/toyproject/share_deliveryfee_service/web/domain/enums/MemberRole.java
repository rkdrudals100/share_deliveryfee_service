package com.toyproject.share_deliveryfee_service.web.domain.enums;

public enum MemberRole {

    USER("일반 회원"), ADMIN("관리자");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
