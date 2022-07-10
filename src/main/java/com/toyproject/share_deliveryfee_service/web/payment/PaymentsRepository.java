package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    Payments findPaymentsById(Long id);

    Payments findByMerchantUid(String merchant_uid);

    Payments findByPayerAndCreateAt(Member member, LocalDateTime createAt);

}
