package com.toyproject.share_deliveryfee_service.web.member.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


//2022-04-20 강경민
//아이디중복체크 폼 구현

@Getter @Setter
@Builder
public class MemberDupCheckIdDto {

    private String memberId;
}
