package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Data;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "delivery_questions")
public class DeliveryQuestion extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "delivery_question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FoodReceive foodReceive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

}
