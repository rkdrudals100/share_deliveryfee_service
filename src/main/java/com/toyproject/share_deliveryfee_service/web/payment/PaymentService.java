package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.Payments;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReasonForPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentsRepository paymentsRepository;




    public void newPayment(Party party, Member member,
                           String merchant_uid, int amount,
                           ReasonForPayment reasonForPayment){
        log.warn(party.getTitle());
        log.warn(member.getUsername());
        log.warn(merchant_uid);
        log.warn(String.valueOf(amount));
        log.warn(String.valueOf(reasonForPayment));

        Payments payments = Payments.builder()
                .merchantUid(merchant_uid)
                .reasonForPayment(reasonForPayment)
                .amount(amount)
                .refundAmount(0)
                .payer(member)
                .party(party)
                .build();

        paymentsRepository.save(payments);

        party.addPayments(payments);
        member.addPayments(payments);
    }
}
