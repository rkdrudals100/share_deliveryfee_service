package com.toyproject.share_deliveryfee_service.web.member.validator;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberDupCheckIdDto;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MemberDupCheckValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MemberDupCheckIdDto memberDupCheckIdDto = (MemberDupCheckIdDto) target;

        if(!StringUtils.hasText(memberDupCheckIdDto.getMemberId())){
            errors.rejectValue("memberId", "required");
        } else if(!Pattern.matches("[a-zA-Z0-9]{4,20}", memberDupCheckIdDto.getMemberId())){ //테스트 후 8~20으로 수정 요망
            errors.rejectValue("memberId", "pattern");
        }


        if (memberRepository.findByUsername(memberDupCheckIdDto.getMemberId()) != null){
            errors.reject("duplicatedId");
        }
    }
}
