package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReasonForPayment;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfMessage;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.partyMessage.PartyMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;

    private final PaymentService paymentService;
    private final PartyMessageService partyMessageService;
    private final NotificationLogService notificationLogService;






    @PostMapping("/payment/{reasonForPayment}")
    public String paymentSuccess(@PathVariable String reasonForPayment, Principal principal, @RequestBody Map<String, Object> inputMap){

        Map<String, String> returnMap = new HashMap<>();

        String messageBody = (String)inputMap.get("getMessageBody");
        int serviceFee = Integer.parseInt(inputMap.get("getServiceFee").toString());
        int deliveryFee = Integer.parseInt(inputMap.get("getDeliveryFee").toString());
        Party party = partyRepository.findById(Long.valueOf(String.valueOf(inputMap.get("getPartyId")))).get();
        Member member = memberRepository.findByUsername(principal.getName());


        paymentService.newPayment(party, member, String.valueOf(inputMap.get("getMerchant_uid")),
                (Integer) inputMap.get("getAmount"), ReasonForPayment.JOINPARTY);
        // ?????? ?????? ?????? ?????? ??????

        // ??????????????? ??????????????? ??????
        partyMessageService.newMessage(party, member, TypeOfMessage.PARTYAPPLICATION,
                messageBody, serviceFee, deliveryFee);

        // ?????????, ????????? ?????? ??????
        notificationLogService.newNotificationLog(memberRepository.findByUsername(principal.getName()),
                "'" + party.getTitle() + "' " + "????????? ??????????????? ?????????????????????.",
                "/accountInfo/notification");

        notificationLogService.newNotificationLog(memberRepository.findByUsername(party.getOrganizer().getUsername()),
                "'" + principal.getName() + "' " + "?????? '" + party.getTitle() + "' ????????? ????????? ?????????????????????.",
                "/partyDetails/" + party.getId());

        // ????????? returnMap?????? ?????? ??????
        return "redirect:/partyDetails/" + party.getId();
    }





}
