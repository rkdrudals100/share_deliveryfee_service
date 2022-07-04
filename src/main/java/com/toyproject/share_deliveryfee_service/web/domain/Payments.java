package com.toyproject.share_deliveryfee_service.web.domain;

import com.toyproject.share_deliveryfee_service.web.domain.enums.ReasonForPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @Table(name = "payments")
@SequenceGenerator(
        name = "PAYMENT_SEQ_GEN", sequenceName = "PAYMENT_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_SEQ_GEN")
    @Column(name = "payment_id")
    private Long id;

    @Column(unique = true)
    private String merchantUid;

    @Enumerated(EnumType.STRING)
    private ReasonForPayment reasonForPayment;

    private int amount;

    private int refundAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

}

