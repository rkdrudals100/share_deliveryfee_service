package com.toyproject.share_deliveryfee_service.web.member.validator;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.member.form.BaseLocationChangeDto;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasePickupLocationChangeValidator implements Validator {

    private final PartyService partyService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        BaseLocationChangeDto baseLocationChangeDto = (BaseLocationChangeDto) target;

        if (baseLocationChangeDto.getChangedBaseLocation().isEmpty()){
            errors.rejectValue("changedBaseLocation", "required");
        } else if (partyService.getLatitudeAndLongitudeFromKakaoMap(baseLocationChangeDto.getChangedBaseLocation()) == null) {
            errors.rejectValue("changedBaseLocation", "incorrect");
        }
    }
}
