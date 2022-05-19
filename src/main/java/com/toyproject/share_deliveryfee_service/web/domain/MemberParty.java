package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberParty extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_party_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;


    // 연관관계 편의 메소드
    public void addMember(Member member){
        this.setMember(member);
        member.getMemberParties().add(this);
    }

    public void addParty(Party party){
        this.setParty(party);
        party.getMemberParties().add(this);
    }

    public void addMemberParty(Party party, Member member){
        addMember(member);
        addParty(party);
    }


}
