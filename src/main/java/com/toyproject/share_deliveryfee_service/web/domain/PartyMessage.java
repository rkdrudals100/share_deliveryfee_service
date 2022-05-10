package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Data;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "party_messages")
public class PartyMessage extends BaseEntity{

    @Id
    @Column(name = "party_message_id")
    private Long id;

    private String requestContents;

    private int price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;
}
