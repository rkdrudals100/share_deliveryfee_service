package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;






//    @PostMapping("hihi")
}
