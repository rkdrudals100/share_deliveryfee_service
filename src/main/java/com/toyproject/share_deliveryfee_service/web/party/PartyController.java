package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.config.ConfigUtil;
import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReasonForPayment;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfMessage;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import com.toyproject.share_deliveryfee_service.web.party.form.PartySearchDto;
import com.toyproject.share_deliveryfee_service.web.party.validator.PartyRegisterValidator;
import com.toyproject.share_deliveryfee_service.web.partyMessage.PartyMessageService;
import com.toyproject.share_deliveryfee_service.web.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PartyController {
    
    private final PartyRegisterValidator partyRegisterValidator;

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final PartyMessageRepository partyMessageRepository;

    private final PartyService partyService;
    private final PartyMessageService partyMessageService;
    private final NotificationLogService notificationLogService;
    private final PaymentService paymentService;

    private final ConfigUtil configUtil;





    //  2022-05-09 강경민
    //  파티 모집글 등록 기능 수정
    @GetMapping("/makeParty")
    public String makeParty(Model model){

        model.addAttribute("partyRegisterDto", new PartyRegisterDto());
        return "makeParty";
    }



    @GetMapping("/test")
    public String test(){
//        partyService.sortByDistanceFromUser();
        partyService.getLatitudeAndLongitudeFromKakaoMap("전북 삼성동 100");

        return "index";
    }





    @PostMapping("/makeParty")
    public String registerParty(@Validated @ModelAttribute PartyRegisterDto partyRegisterDto, BindingResult bindingResult, Principal principal){

        partyRegisterValidator.validate(partyRegisterDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "makeParty";
        }

        Party saveParty = partyService.createParty(principal.getName(), partyRegisterDto);

        log.info("생성된 파티를 '{}'번으로 저장 완료", saveParty.getId());

        notificationLogService.newNotificationLog(memberRepository.findByUsername(principal.getName()),
                "'" + saveParty.getTitle() + "' " + "파티가 생성되었습니다.",
                "/partyDetails/" + saveParty.getId());

        return "redirect:/";
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
    public String searchParty(Model model, Principal principal){

        List<Party> parties = partyRepository.findByPartyStatus(PartyStatus.RECRUITING, Sort.by(Sort.Direction.ASC, "pickUpLocation"));


        if (memberRepository.findByUsername(principal.getName()).getBaseLocation() == null){
            model.addAttribute("parties", parties);
            model.addAttribute("basePickupLocationRegister", false);
        } else {
            List<PartySearchDto> partySearchDtos = partyService.calculateAndAddDistance(memberRepository.findByUsername(principal.getName()), parties);
            model.addAttribute("parties",
                    partySearchDtos.stream().sorted(Comparator.comparing(PartySearchDto::getDistanceFromMemberBaseLocation)).collect(Collectors.toList()));
            model.addAttribute("basePickupLocationRegister", true);
        }

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











    @PostMapping("/partyDetails/{partyId}/PartyJoin")
    public String PartyJoin(@PathVariable Long partyId, @RequestBody Map<String, String> inputMap){

        Long partyMessageId = Long.valueOf(inputMap.get("getPartyMessageId"));
        String choice = inputMap.get("getChoice");

        String k = partyMessageService.processPartyJoin(partyId, partyMessageId, choice);

        //k가 overcrowding일 시 bindingresult 출력
        log.info(k);


        
        return "redirect:/partyDetails/" + partyId;
    }









    @PostMapping("/partyDetails/{partyId}/partyStatusChange")
    @ResponseBody
    public Map<String, String> changePartyStatus(@PathVariable Long partyId, @RequestBody Map<String, String> inputMap, Principal principal){

        Map<String, String> returnMap = new HashMap<>();

        String getClickedBtn = inputMap.get("getClickedBtn");
        Party party = partyRepository.findPartyById(partyId);
        Member member = memberRepository.findByUsername(principal.getName());

        returnMap.put("partyStatusDescription", partyService.updatePartyStatus(party, getClickedBtn).getDescription());

        partyMessageService.newMessage(party, member, TypeOfMessage.STATUSCHANGE,
                null, 0, 0);

        for (MemberParty eachMemberParty : party.getMemberParties()) {
            if (eachMemberParty.getMember() != member) {
                notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                        "'" + party.getTitle() + "' " + "파티 상태가 '" + getClickedBtn + "'으로 변경되었습니다.",
                        "/partyDetails/" + party.getId());
            } else {
                notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                        "'" + party.getTitle() + "' " + "파티 상태를 '" + getClickedBtn + "'으로 변경하였습니다.",
                        "/partyDetails/" + party.getId());
            }
        }

        return returnMap;
    }























}
