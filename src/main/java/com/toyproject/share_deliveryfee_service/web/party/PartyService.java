package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                .membersNum(1)
                .maxMemberNum(partyRegisterDto.getMaxMemberNum())
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





    public int changePageContents(int currentPageNum,  int totalPageNum,  String whichBtn){
        int printCardNum = 0;

        if (whichBtn.equals("right")) {
            if ((currentPageNum) * 6 <= totalPageNum) {
                printCardNum = 6;
            } else {
                printCardNum = 6 - ((currentPageNum * 6) - totalPageNum);
            }
        }else{
            printCardNum = 6;
        }

        return printCardNum;
    }





    // 파티 검색
    public List<Party> searchPartyByKeywords(String keyWord){
        String[] keywords = keyWord.split("\\s", 2);
        List<Party> parties = new ArrayList<>();

        if (keywords.length > 1){
            List<Party> parties1 = partyRepository.findBySearch(PartyStatus.RECRUITING, keywords[0], Sort.by(Sort.Direction.ASC, "pickUpLocation"));
            List<Party> parties2 = partyRepository.findBySearch(PartyStatus.RECRUITING, keywords[1], Sort.by(Sort.Direction.ASC, "pickUpLocation"));

            for (Party party: parties1){
                if (parties2.contains(party)){
                    parties.add(party);
                }
            }
        } else {
            parties = partyRepository.findBySearch(PartyStatus.RECRUITING, keyWord, Sort.by(Sort.Direction.ASC, "pickUpLocation"));
        }

        return parties;
    }





    // 유저가 파티에 가입했는지, 파티원인지, 파티장인지 판별
    public String determineUserRoleAtParty(Member member, Party party){
        String userRole = "notJoined";

        List<MemberParty> memberJoinedParty = memberPartyRepository.findByMember(member);
        List<MemberParty> partyMembers = memberPartyRepository.findByParty(party);

        int sizeOfJoinedParty = memberJoinedParty.size();
        memberJoinedParty.removeAll(partyMembers);
        int sizeOfJoinedParty2 = memberJoinedParty.size();

        if(sizeOfJoinedParty == sizeOfJoinedParty2){
            return userRole;
        }   else {
            userRole = "joined";
        }

        if (party.getOrganizer() == member){
            userRole = "organizer";
        }

        return userRole;
    }




    public PartyStatus updatePartyStatus(Party party, String partyDescription){

        PartyStatus findPartyStatus = PartyStatus.RECRUITING;

        if (partyDescription.equals("모집 중")){
            findPartyStatus = PartyStatus.RECRUITING;
        } else if (partyDescription.equals("모집 완료")){
            findPartyStatus = PartyStatus.RECCOMPLETED;
        } else if (partyDescription.equals("주문 완료")){
            findPartyStatus = PartyStatus.ORDERED;
        } else if (partyDescription.equals("배달 완료")){
            findPartyStatus = PartyStatus.DELCOMPLETED;
        }

        return party.updatePartyStatus(findPartyStatus);
    }

}
