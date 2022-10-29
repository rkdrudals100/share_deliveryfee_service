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
import com.toyproject.share_deliveryfee_service.web.payment.PaymentsRepository;
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
    private final PaymentsRepository paymentsRepository;

    private final PartyService partyService;
    private final PartyMessageService partyMessageService;
    private final NotificationLogService notificationLogService;
    private final PaymentService paymentService;

    private final ConfigUtil configUtil;







    @GetMapping("/newParty")
    public String makeParty(Model model){

        model.addAttribute("partyRegisterDto", new PartyRegisterDto());
        return "makeParty";
    }






    @PostMapping("/newParty")
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
                "/party/" + saveParty.getId());

        return "redirect:/";
    }






    @GetMapping("/search/party")
    public String getContent2(@RequestParam String keyWord, Model model, Principal principal) {

        List<Party> parties = partyService.searchPartyByKeywords(keyWord);

        // 검색 결과가 있는지 확인
        if (parties.isEmpty()){
            parties.add(Party.builder().title("null").build());
            model.addAttribute("isNull", "true");
        } else{
            model.addAttribute("isNull", "false");
        }

        // 거리 측정을 사용할 수 있는지 확인
        if (memberRepository.findByUsername(principal.getName()).getBaseLocation() != null && model.getAttribute("isNull") != "true"){
            List<PartySearchDto> partySearchDtos = partyService.calculateAndAddDistance(memberRepository.findByUsername(principal.getName()), parties);
            model.addAttribute("parties",
                    partySearchDtos.stream().sorted(Comparator.comparing(PartySearchDto::getDistanceFromMemberBaseLocation)).collect(Collectors.toList()));
            model.addAttribute("basePickupLocationRegister", true);
        } else{
            model.addAttribute("parties", parties);
            model.addAttribute("basePickupLocationRegister", false);
        }

        model.addAttribute("totalResultNum", parties.size() );
        model.addAttribute("keyword", keyWord);

        return "fragments :: searchParty";
    }





    @GetMapping("/search")
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






    @GetMapping("/search/party/page/{currentPageNum}")
    @ResponseBody
    public Map<String, String> changePageCards(@PathVariable int currentPageNum, @RequestParam int totalPageNum, @RequestParam String whichBtn){

        Map<String, String> returnMap = new HashMap<>();

        int printCardNum = partyService.changePageContents(currentPageNum, totalPageNum, whichBtn);

        returnMap.put("printCardNum", String.valueOf(printCardNum));

        return returnMap;
    }





    @GetMapping("/party/{partyId}")
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











    @PostMapping("/party/{partyId}/joined/user")
    public String PartyJoin(@PathVariable Long partyId, @RequestBody Map<String, String> inputMap){

        Long partyMessageId = Long.valueOf(inputMap.get("getPartyMessageId"));
        String choice = inputMap.get("getChoice");

        String k = partyMessageService.processPartyJoin(partyId, partyMessageId, choice);

        //k가 overcrowding일 시 bindingresult 출력
        log.info(k);


        
        return "redirect:/party/" + partyId;
    }









    @PatchMapping("/party/{partyId}/partyStatus")
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
                        "/party/" + party.getId());
            } else {
                notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                        "'" + party.getTitle() + "' " + "파티 상태를 '" + getClickedBtn + "'으로 변경하였습니다.",
                        "/party/" + party.getId());
            }
        }

        return returnMap;
    }























}
