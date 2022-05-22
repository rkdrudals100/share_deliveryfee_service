package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import com.toyproject.share_deliveryfee_service.web.party.validator.PartyRegisterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PartyController {

    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final MemberRepository memberRepository;
    private final PartyRegisterValidator partyRegisterValidator;
    private final PartyService partyService;

    @GetMapping("/searchParty")
    public String searchParty(Model model){

        List<Party> parties = partyRepository.findByPartyStatus(PartyStatus.RECRUITING, Sort.by(Sort.Direction.ASC, "pickUpLocation"));

        model.addAttribute("parties", parties);

        return "searchParty";
    }



    //  2022-05-09 강경민
    //  파티 모집글 등록 기능 수정
    @GetMapping("/makeParty")
    public String makeParty(Model model){

        model.addAttribute("partyRegisterDto", new PartyRegisterDto());
        return "makeParty";
    }

    @GetMapping("/testsavememberParty")
    public String hihi(Principal principal){
        Member loginMember = memberRepository.findByUsername(principal.getName());
        log.info(String.valueOf(loginMember.getHostedparties().isEmpty()));
        log.info(String.valueOf(loginMember.getMemberParties().isEmpty()));

        return "index";
    }


    @PostMapping("/makeParty")
    public String registerParty(@Validated @ModelAttribute PartyRegisterDto partyRegisterDto, BindingResult bindingResult, Principal principal){

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
        log.info(partyRegisterDto.getLimitTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        log.info(String.valueOf(partyRegisterDto.getDeliveryPlatform()));

        Party saveParty = partyService.createParty(principal.getName(), partyRegisterDto);

        log.info("생성된 파티를 '{}'번으로 저장 완료", saveParty.getId());

        return "index";
    }
}
