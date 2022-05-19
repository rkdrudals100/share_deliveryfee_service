package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final MemberPartyRepository memberPartyRepository;

    
    // 2022-05-19 강경민
    // 파티 저장 기능 구현
    public Party createParty(String username, PartyRegisterDto partyRegisterDto){

        Member member = memberRepository.findByUsername(username);

        DeliveryPlatform getDeliveryPlatform;

        if(partyRegisterDto.getDeliveryPlatform() == 0){
            getDeliveryPlatform = DeliveryPlatform.BM;
        } else if(partyRegisterDto.getDeliveryPlatform() == 1){
            getDeliveryPlatform = DeliveryPlatform.YGY;
        } else if(partyRegisterDto.getDeliveryPlatform() == 2){
            getDeliveryPlatform = DeliveryPlatform.BT;
        } else {
            getDeliveryPlatform = DeliveryPlatform.BM;
        }

        Party saveParty = Party.builder()
                .title(partyRegisterDto.getTitle())
                .pickUpLocation(partyRegisterDto.getPickUpLocation())
                .pickUpLocationDetail(partyRegisterDto.getPickUpLocationDetail())
                .restaurant(partyRegisterDto.getRestaurant())
                .introduction(partyRegisterDto.getIntroduction())
                .totalPrice(partyRegisterDto.getTotalPrice())
                .membersNum(partyRegisterDto.getMembersNum())
                .limitTime(partyRegisterDto.getLimitTime())
                .deliveryPlatform(getDeliveryPlatform)
                .partyStatus(PartyStatus.RECRUITING)
                .build();

        saveParty.changeOrganizer(member);


        MemberParty saveMemberParty = MemberParty.builder()
                .member(member)
                .party(saveParty)
                .build();

        memberPartyRepository.save(saveMemberParty);

        return partyRepository.save(saveParty);
    }

}
