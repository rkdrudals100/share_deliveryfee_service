package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @ToString(callSuper = true, exclude = {})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    @Column(unique = true)
    private String phoneNum;

    private String baseLocation;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
    private String memberRoles;

    //평판 항목 추가

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationLog> notificationLogs = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberParty> memberParties = new ArrayList<>();

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Party> hostedparties = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartyMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryQuestion> deliveryQuestions = new ArrayList<>();


    public List<String> getRoleList(){
        if(this.memberRoles.length() > 0){
            return Arrays.asList(this.memberRoles.split(","));
        }
        return new ArrayList<>();
    }

    // 연관관계 편의 메소드
    public void addNotificationLogs(NotificationLog notificationLog){
        notificationLog.setMember(this);
        this.getNotificationLogs().add(notificationLog);
    }

    public void addMessages(PartyMessage partyMessage){
        partyMessage.setMember(this);
        this.getMessages().add(partyMessage);
    }

    public void  addDeliveryQuestion(DeliveryQuestion deliveryQuestion){
        deliveryQuestion.setMember(this);
        this.getDeliveryQuestions().add(deliveryQuestion);
    }

    public void addMemberParties(MemberParty memberParty){
        memberParty.setMember(this);
        this.getMemberParties().add(memberParty);
    }

}
