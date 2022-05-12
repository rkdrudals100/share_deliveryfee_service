package com.toyproject.share_deliveryfee_service.web.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

//2022-04-20 강경민
//회원가입 폼 구현

@Getter @Setter
public class MemberRegisterDto {

    private String memberId;

    private String memberEmail;

    private String memberPhoneNum;

    private String phoneCertificationNum;

    private String memberPassword;

    private String memberPassword2;
}
