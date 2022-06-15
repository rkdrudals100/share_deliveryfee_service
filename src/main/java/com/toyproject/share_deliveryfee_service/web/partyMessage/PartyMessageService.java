package com.toyproject.share_deliveryfee_service.web.partyMessage;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyMessageRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartyMessageService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final PartyMessageRepository partyMessageRepository;





    public void newMessage(Party party, Member member,
                           TypeOfMessage typeOfMessage,
                           String messageBody,
                           int serviceFee,
                           int deliveryFee){
        PartyMessage partyMessage = PartyMessage.builder()
                .member(member)
                .party(party)
                .processingStatus(ProcessingStatus.NOTYET)
                .typeOfMessage(typeOfMessage)
                .messageBody(messageBody)
                .serviceFee(serviceFee)
                .deliveryFee(deliveryFee)
                .price(serviceFee + deliveryFee)
                .build();

        partyMessageRepository.save(partyMessage);

        member.addMessages(partyMessage);
        party.addPartyMessage(partyMessage);
    }







    public String processPartyJoin(Long partyId, Long partyMessageId, String choice){

        Party party = partyRepository.findById(partyId).get();
        PartyMessage partyMessage = partyMessageRepository.findById(partyMessageId).get();
        Member member = partyMessage.getMember();

        partyMessage.updateProcessingStatus(ProcessingStatus.PROCESSED);

        if (choice.equals("yes")){
            MemberParty memberParty = MemberParty.builder()
                    .member(member)
                    .party(party)
                    .build();

            memberParty.addMemberParty(party, member);
            party.updateMembersNum("up");
        }
        return "done";
    }



}
