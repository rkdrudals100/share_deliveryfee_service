package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "party_messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyMessage extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "party_message_id")
    private Long id;

    private String messageBody;

    private int serviceFee;

    private int deliveryFee;

    private int price;

    @Enumerated(EnumType.STRING)
    private TypeOfMessage typeOfMessage;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;




    
    // 필드 변경 메소드
    public ProcessingStatus updateProcessingStatus(ProcessingStatus processingStatus){
        this.processingStatus = processingStatus;

        return this.processingStatus;
    }

}
