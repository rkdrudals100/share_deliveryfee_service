package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "parties")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "party_id")
    private Long id;

    private String title;

    private String introduction;

    private String pickUpLocation;

    private String pickUpLocationDetail;

    private String restaurant;

    private int totalPrice;

    private int membersNum;

    private int maxMemberNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime limitTime;

    @Enumerated(EnumType.STRING)
    private DeliveryPlatform deliveryPlatform;

    @Enumerated(EnumType.STRING)
    private PartyStatus partyStatus;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryQuestion> deliveryQuestion = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyMessage> partyMessages = new ArrayList<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodCategory> foodCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member organizer;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberParty> memberParties = new ArrayList<>();


    // 연관관계 편의 메소드
    public void changeOrganizer(Member member){
        this.organizer = member;
        member.getHostedparties().add(this);
    }

    public void addPartyMessage(PartyMessage partyMessage){
        this.getPartyMessages().add(partyMessage);
        partyMessage.setParty(this);
    }

    public void addDeliveryQuestion(DeliveryQuestion deliveryQuestion){
        this.getDeliveryQuestion().add(deliveryQuestion);
        deliveryQuestion.setParty(this);
    }

    public void addFoodCategory(FoodCategory foodCategory){
        this.getFoodCategories().add(foodCategory);
        foodCategory.setParty(this);
    }

}
