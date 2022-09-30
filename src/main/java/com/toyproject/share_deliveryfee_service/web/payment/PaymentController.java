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

        String messageBody = (String)inputMap.get( "getMessageBody");
        int serviceFee = Integer.parseInt(inputMap.get("getServiceFee").toString());
        int deliveryFee = Integer.parseInt(inputMap.get("getDeliveryFee").toString());
        Party party = partyRepository.findById(Long.valueOf(String.valueOf(inputMap.get("getPartyId")))).get();
        Member member = memberRepository.findByUsername(principal.getName());


        paymentService.newPayment(party, member, String.valueOf(inputMap.get("getMerchant_uid")),
                (Integer) inputMap.get("getAmount"), ReasonForPayment.JOINPARTY);
        // 결제 검증 코드 추가 요망


        // 파티장에게 파티메시지 전송
        partyMessageService.newMessage(party, member, TypeOfMessage.PARTYAPPLICATION,
                messageBody, serviceFee, deliveryFee);

        // 신청자, 파티장 로그 작성
        notificationLogService.newNotificationLog(memberRepository.findByUsername(principal.getName()),
                "'" + party.getTitle() + "' " + "파티에 가입신청이 완료되었습니다.",
                "/accountInfo/notification");

        notificationLogService.newNotificationLog(memberRepository.findByUsername(party.getOrganizer().getUsername()),
                "'" + principal.getName() + "' " + "님이 '" + party.getTitle() + "' 파티에 가입을 신청하셨습니다.",
                "/partyDetails/" + party.getId());

        // 리턴값 returnMap으로 변경 요망
        return "redirect:/partyDetails/" + party.getId();
    }





}
