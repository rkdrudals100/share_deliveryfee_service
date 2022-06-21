package com.toyproject.share_deliveryfee_service.web.partyMessage;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.PartyMessageRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartyMessageService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final PartyMessageRepository partyMessageRepository;

    private final PartyService partyService;
    private final NotificationLogService notificationLogService;



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







    public List<PartyMessage> getPartyMessages(Party party, String username){

        List<PartyMessage> partyMessages;

        if (party.getOrganizer().getUsername().equals(username)){
            partyMessages = partyMessageRepository.findByPartyAndAndProcessingStatusOrderByIdDesc(party, ProcessingStatus.NOTYET);
        }   else{
            partyMessages = partyMessageRepository.findByPartyAndProcessingStatusAndTypeOfMessageNotLikeOrderByIdDesc(party, ProcessingStatus.NOTYET, TypeOfMessage.PARTYAPPLICATION);
        }

        return partyMessages;
    }







    public String processPartyJoin(Long partyId, Long partyMessageId, String choice){

        Party party = partyRepository.findById(partyId).get();
        PartyMessage partyMessage = partyMessageRepository.findById(partyMessageId).get();
        Member member = partyMessage.getMember();

        partyMessage.updateProcessingStatus(ProcessingStatus.PROCESSED);

        // 파티장이 참가 요청 수락 시
        if (choice.equals("yes")){
            if (party.getMembersNum() < party.getMaxMemberNum()) {
                MemberParty memberParty = MemberParty.builder()
                        .member(member)
                        .party(party)
                        .build();

                memberParty.addMemberParty(party, member);
                party.updateMembersNum("up");
                newMessage(party, member, TypeOfMessage.PARTYJOIN, null, 0, 0);
                log.info("파티 참가 수락");

                notificationLogService.newNotificationLog(member,
                        "'" + party.getTitle() + "' " + "파티에 가입하셨습니다.",
                        "/partyDetails/" + party.getId());

                for (MemberParty eachMemberParty : party.getMemberParties()) {
                    if (eachMemberParty.getMember() != member) {
                        notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                                "'" + member.getUsername() + "'님의 '" + party.getTitle() + "' " + "파티가입요청이 수락되었습니다.",
                                "/partyDetails/" + party.getId());
                    }
                }

                // 파티가 꽉차면 '모집완료' 로 상태 변경
                if (!(party.getMaxMemberNum() > party.getMembersNum())){
                    partyService.updatePartyStatus(party, "모집 완료");

                    newMessage(party, member, TypeOfMessage.STATUSCHANGE, null, 0, 0);

                    for (MemberParty eachMemberParty : party.getMemberParties()) {
                        notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                                "'" + party.getTitle() + "' " + "파티 상태가 '모집 완료'으로 변경되었습니다.",
                                "/partyDetails/" + party.getId());
                    }

                }
            } else {
                log.info("파티 인원 초과");
                notificationLogService.newNotificationLog(member,
                        "'" + party.getTitle() + "' " + "파티 인원이 초과되어 가입이 거절되었습니다.",
                        "/partyDetails/" + party.getId());
                return "overcrowding";
            }
        } else if (choice.equals("no")){
            log.info("파티 참가 거절");
            notificationLogService.newNotificationLog(member,
                    "'" + party.getTitle() + "' " + "파티가입요청이 거절되었습니다.",
                    "/partyDetails/" + party.getId());

            notificationLogService.newNotificationLog(party.getOrganizer(),
                    "'" + member.getUsername() + "'님의 '" + party.getTitle() + "' " + "파티가입요청을 거절하셨습니다.",
                    "/partyDetails/" + party.getId());
        }
        return "success";
    }



}
