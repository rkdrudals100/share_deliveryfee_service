package com.toyproject.share_deliveryfee_service.web.member.validator;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.member.MemberService;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

//2022-04-21 강경민
//멤버 저장 시 검증 작성

@Component
@RequiredArgsConstructor
public class MemberRegisterValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberRegisterDto memberRegisterDto = (MemberRegisterDto) target;

        if(!StringUtils.hasText(memberRegisterDto.getMemberId())){
            errors.rejectValue("memberId", "required");
        } else if(!Pattern.matches("[a-zA-Z0-9]{4,20}", memberRegisterDto.getMemberId())){ //테스트 후 8~20으로 수정 요망
            errors.rejectValue("memberId", "pattern");
        } 

        if(!StringUtils.hasText(memberRegisterDto.getMemberEmail())){
            errors.rejectValue("memberEmail", "required");
        } else if(!Pattern.matches("[a-zA-Z0-9]+.+@[a-zA-Z]{3,15}[.].{3,10}", memberRegisterDto.getMemberEmail())){
            errors.rejectValue("memberEmail", "pattern");
        }

        if(!StringUtils.hasText(memberRegisterDto.getMemberPhoneNum())) {
            errors.rejectValue("memberPhoneNum", "required");
        } else if(!Pattern.matches("01.{9}", memberRegisterDto.getMemberPhoneNum())) {
            errors.rejectValue("memberPhoneNum", "pattern");
        }

        if(!StringUtils.hasText(memberRegisterDto.getMemberPassword())){
            errors.rejectValue("memberPassword", "required");
        } else if(memberRegisterDto.getMemberPassword().length() < 4 || memberRegisterDto.getMemberPassword().length() > 20){
            errors.rejectValue("memberPassword", "size");
        }

        if(!StringUtils.hasText(memberRegisterDto.getMemberPassword2())){
            errors.rejectValue("memberPassword2", "required");
        } else if(!(memberRegisterDto.getMemberPassword().equals(memberRegisterDto.getMemberPassword2()))){
            errors.rejectValue("memberPassword2", "unequal");
        }

        // 글로벌에러
        if (memberRepository.findByUsername(memberRegisterDto.getMemberId()) != null){
            errors.reject("duplicatedId");
        }
        else if(memberRepository.findByPhoneNum(memberRegisterDto.getMemberPhoneNum()) != null){
            errors.reject("phoneNumAlreadyExist");
        }
    }
}
