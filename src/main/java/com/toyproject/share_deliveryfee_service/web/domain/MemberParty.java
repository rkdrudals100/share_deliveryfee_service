package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Data;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data
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
    public void addMember(Party party){
        party.getMemberParties().add(this);
        this.setParty(party);
    }

    public void addParty(Member member){
        member.getMemberParties().add(this);
        this.setMember(member);
    }

    public void addMemberParty(Party party, Member member){
        addMember(party);
        addParty(member);
    }

}
