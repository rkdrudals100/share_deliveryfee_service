package com.toyproject.share_deliveryfee_service.web.domain;

public enum TypeOfMessage {
    PARTYAPPLICATION("파티가입신청", "님이 파티가입을 신청하셨습니다."), PARTYJOIN("파티 가입 완료", "님이 파티에 참가하셨습니다."),
    STATUSCHANGE("파티상태변경", "님이 파티상태를 변경하였습니다.");

    private final String description;
    private final String htmlDescription;

    TypeOfMessage(String description, String htmlDescription) {
        this.description = description;
        this.htmlDescription = htmlDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }
}
