package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import com.toyproject.share_deliveryfee_service.web.party.validator.PartyRegisterValidator;
import com.toyproject.share_deliveryfee_service.web.partyMessage.PartyMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PartyController {

    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final MemberRepository memberRepository;
    private final PartyRegisterValidator partyRegisterValidator;
    private final PartyService partyService;
    private final PartyMessageRepository partyMessageRepository;
    private final PartyMessageService partyMessageService;





    //  2022-05-09 강경민
    //  파티 모집글 등록 기능 수정
    @GetMapping("/makeParty")
    public String makeParty(Model model){

        model.addAttribute("partyRegisterDto", new PartyRegisterDto());
        return "makeParty";
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






    @PostMapping("/searchParty/search")
    public String getContent2(@RequestBody Map<String, Object> inputMap, Model model) {
        String keyWord = (String)inputMap.get("getSearchWord");

        List<Party> parties = partyService.searchPartyByKeywords(keyWord);

        if (parties.isEmpty()){
            parties.add(Party.builder().title("null").build());
            model.addAttribute("isNull", "true");
        } else{
            model.addAttribute("isNull", "false");
        }
        log.info(parties.get(0).getTitle());
        model.addAttribute("parties", parties);
        model.addAttribute("totalResultNum", parties.size());
        model.addAttribute("keyword", keyWord);

        return "fragments :: searchParty";
    }





    @GetMapping("/searchParty")
    public String searchParty(Model model){

        List<Party> parties = partyRepository.findByPartyStatus(PartyStatus.RECRUITING, Sort.by(Sort.Direction.ASC, "pickUpLocation"));

        model.addAttribute("parties", parties);

        return "searchParty";
    }






    @PostMapping("/changePageCards")
    @ResponseBody
    public Map<String, String> changePageCards(@RequestBody Map<String, Object> inputMap){
        Map<String, String> returnMap = new HashMap<>();

        int printCardNum = partyService.changePageContents(Integer.parseInt((String)inputMap.get("getCurrentPageNum")),
                Integer.parseInt((String)inputMap.get("getTotalPageNum")), (String)inputMap.get("whichBtn"));

        returnMap.put("printCardNum", String.valueOf(printCardNum));

        return returnMap;
    }





    @GetMapping("/partyDetails/{partyId}")
    public String detail(@PathVariable Long partyId, Model model, Principal principal){

        Party getParty = partyRepository.findPartyById(partyId);

        String userRoleAtParty = partyService.determineUserRoleAtParty(memberRepository.findByUsername(principal.getName()), getParty);
        List<PartyMessage> partyMessages = partyMessageService.getPartyMessages(getParty, principal.getName());

        if (getParty == null){
            log.info("잘못된 접근, 파티가 존재하지 않음"); // 에러 페이지로 이동하도록 수정
            return "errorPage";
        } else {
            model.addAttribute("party", getParty);
            model.addAttribute("userRoleAtParty", userRoleAtParty);
            model.addAttribute("partyMessages", partyMessages);
        }

        return "partyDetails";
    }






    @PostMapping("/partyDetails/{partyId}/paymentSuccess")
    public String paymentSuccess(@PathVariable Long partyId, Principal principal, @RequestBody Map<String, Object>inputMap){

        Map<String, String> returnMap = new HashMap<>();

        String messageBody = (String)inputMap.get("getMessageBody");
        int serviceFee = Integer.parseInt(inputMap.get("getServiceFee").toString());
        int deliveryFee = Integer.parseInt(inputMap.get("getDeliveryFee").toString());

        Party party = partyRepository.findById(partyId).get();
        Member member = memberRepository.findByUsername(principal.getName());
        // 결제 검증 코드
        // 파티장에게 메시지 날림
        partyMessageService.newMessage(party, member, TypeOfMessage.PARTYAPPLICATION,
                messageBody, serviceFee, deliveryFee);
        
        return "redirect:/partyDetails/" + partyId;
    }




    @PostMapping("/partyDetails/{partyId}/PartyJoin")
    public String PartyJoin(@PathVariable Long partyId, @RequestBody Map<String, String> inputMap){

        Long partyMessageId = Long.valueOf(inputMap.get("getPartyMessageId"));
        String choice = inputMap.get("getChoice");

        String k = partyMessageService.processPartyJoin(partyId, partyMessageId, choice);

        //k가 overcrowding일 시 bindingresult 출력
        log.info(k);

        // 성공 파티메시지 출력
        return "redirect:/partyDetails/" + partyId;
    }























}
