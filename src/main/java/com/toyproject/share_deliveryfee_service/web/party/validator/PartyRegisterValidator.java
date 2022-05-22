package com.toyproject.share_deliveryfee_service.web.party.validator;

import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PartyRegisterValidator implements Validator {

    private final PartyRepository partyRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return Party.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PartyRegisterDto partyRegisterDto = (PartyRegisterDto) target;

        if (!StringUtils.hasText(partyRegisterDto.getTitle())) {
            errors.rejectValue("title", "required");
        } else if (partyRegisterDto.getTitle().length() > 100) {
            errors.rejectValue("title", "size");
        }

        if (!StringUtils.hasText(partyRegisterDto.getPickUpLocation())) {
            errors.rejectValue("pickUpLocation", "required");
        }

        if (!StringUtils.hasText(partyRegisterDto.getPickUpLocationDetail())) {
            errors.rejectValue("pickUpLocationDetail", "required");
        }

        if (!StringUtils.hasText(partyRegisterDto.getRestaurant())) {
            errors.rejectValue("restaurant", "required");
        }

        if (partyRegisterDto.getIntroduction().length() > 500) {
            errors.rejectValue("introduction", "size");
        }

        if (partyRegisterDto.getTotalPrice() < 100 || partyRegisterDto.getTotalPrice() > 20000 ) {
            errors.rejectValue("totalPrice", "range", new Object[]{100, 20000}, null);
        } else if (partyRegisterDto.getTotalPrice() % 100 != 0){
            errors.rejectValue("totalPrice", "form");
        }

        if (partyRegisterDto.getMaxMemberNum() < 2 || partyRegisterDto.getMaxMemberNum() > 4) {
            errors.rejectValue("maxMemberNum", "range", new Object[]{2, 4}, null);
        }

        if (!StringUtils.hasText(partyRegisterDto.getLimitTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")))) {
            errors.rejectValue("limitTime", "required");
        }

        if (partyRegisterDto.getDeliveryPlatform() < 0 || partyRegisterDto.getDeliveryPlatform() > 2) {
            errors.rejectValue("deliveryPlatform", "badAccess");
        }
    }
}
