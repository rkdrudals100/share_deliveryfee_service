package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import com.toyproject.share_deliveryfee_service.web.party.validator.PartyRegisterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PartyController {

    private final PartyRepository partyRepository;
    private final PartyRegisterValidator partyRegisterValidator;

    @GetMapping("/searchParty")
    public String searchParty(){

        return "searchParty";
    }



    //  2022-05-09 강경민
    //  파티 모집글 등록 기능 수정
    @GetMapping("/makeParty")
    public String makeParty(Model model){

        model.addAttribute("partyRegisterDto", new PartyRegisterDto());
        return "makeParty";
    }


    @PostMapping("/makeParty")
    public String registerParty(@Validated @ModelAttribute PartyRegisterDto partyRegisterDto, BindingResult bindingResult){

        partyRegisterValidator.validate(partyRegisterDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "makeParty";
        }
        log.info(partyRegisterDto.getTitle());
        log.info(partyRegisterDto.getPickUpLocation());
        log.info(partyRegisterDto.getPickUpLocationDetail());
        log.info(partyRegisterDto.getRestaurant());
        log.info(partyRegisterDto.getIntroduction());
        log.info(String.valueOf(partyRegisterDto.getTotalPrice()));
        log.info(String.valueOf(partyRegisterDto.getMembersNum()));
        log.info(partyRegisterDto.getLimitTime());
        log.info(String.valueOf(partyRegisterDto.getDeliveryPlatform()));


        return "index";
    }
}
