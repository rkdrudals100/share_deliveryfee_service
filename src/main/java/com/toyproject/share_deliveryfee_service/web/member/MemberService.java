package com.toyproject.share_deliveryfee_service.web.member;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.MemberRole;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PartyService partyService;






    public Member registerMember(String username, String password, String email, String phoneNum,
                                 MemberRole memberRole, String memberRoles){

        Member newMember = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .phoneNum(phoneNum)
                .baseLocation(null)
                .latitude(0.0)
                .longitude(0.0)
                .memberRole(memberRole)
                .memberRoles(memberRoles)
                .build();

        memberRepository.save(newMember);

        return memberRepository.findByUsername(username);
    }








    public Map<String, String> DupCheckByUserId(String userId, Map<String, String> returnMap){

        returnMap.put("beforeMemberId", userId);

        if (memberRepository.findByUsername(userId) == null){
            returnMap.put("duplicate", "false");
            log.debug("'{}' 아이디 중복 체크, 중복 없음", userId);
        } else {
            returnMap.put("duplicate", "true");
            log.debug("'{}' 아이디 중복 체크, 중복 발생", userId);
        }

        return returnMap;
    }





    public List<Object> DivideIntoClosedAndOngoingParties(List<MemberParty> memberParties){

        List<Party> ongoingParties = new ArrayList<>();
        List<Party> closedParties = new ArrayList<>();

        for (MemberParty memberParty: memberParties) {
            Party party = memberParty.getParty();

            if (party.getPartyStatus() != PartyStatus.DELCOMPLETED){
                ongoingParties.add(party);
            } else {
                closedParties.add(party);
            }
        }
        return new ArrayList<>(Arrays.asList(ongoingParties, closedParties));
    }




    public void ChangeBaseLocationAndLatitudeAndLongitude(Member member, String baseLocation){

        Map<String, Double> LatitudeAndLongiTude = partyService.getLatitudeAndLongitudeFromKakaoMap(baseLocation);

        member.changeBaseLocationAndLatitudeAndLongitude(baseLocation, LatitudeAndLongiTude.get("latitude"), LatitudeAndLongiTude.get("longitude"));
    }

}
