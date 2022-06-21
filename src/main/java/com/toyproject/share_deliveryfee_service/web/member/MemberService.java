package com.toyproject.share_deliveryfee_service.web.member;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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








    public Member registerMember(String username, String password, String email, String phoneNum,
                               MemberRole memberRole, String memberRoles){

        Member newMember = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .phoneNum(phoneNum)
                .memberRole(memberRole)
                .memberRoles(memberRoles)
                .build();

        memberRepository.save(newMember);

        return memberRepository.findByUsername(username);
    }








    public Map<String, String> DupCheckByUserId(String userId, Map<String, String> returnMap){

        returnMap.put("beforeMemberId", userId);

        if (memberRepository.findByUsername(userId) == null){
            returnMap.put("dupCheckId", "true");
            log.info("'{}' 아이디 중복 체크, 중복 없음", userId);
        } else {
            returnMap.put("dupCheckId", "false");
            log.info("'{}' 아이디 중복 체크, 중복 발생", userId);
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

}
