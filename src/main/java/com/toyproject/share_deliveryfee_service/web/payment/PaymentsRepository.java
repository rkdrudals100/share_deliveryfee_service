package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    Payments findPaymentsById(Long id);

    Payments findByMerchantUid(String merchant_uid);

}
