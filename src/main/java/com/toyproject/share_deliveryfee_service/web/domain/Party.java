package com.toyproject.share_deliveryfee_service.web.domain;

import com.toyproject.share_deliveryfee_service.web.domain.enums.DeliveryPlatform;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
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

    private Double latitude;

    private Double longitude;

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

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payments> payments = new ArrayList<>();

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

    public void addPayments(Payments payments){
        this.getPayments().add(payments);
        payments.setParty(this);
    }





    // 필드 변경 메소드
    public void updateMembersNum(String upOrDown){
        if(upOrDown.equals("up")){
            this.membersNum = this.membersNum + 1;
        } else if(upOrDown.equals("down")){
            this.membersNum = this.membersNum - 1;
        }
    }

    public PartyStatus updatePartyStatus(PartyStatus partyStatus){
        this.partyStatus = partyStatus;

        return this.partyStatus;
    }
}
