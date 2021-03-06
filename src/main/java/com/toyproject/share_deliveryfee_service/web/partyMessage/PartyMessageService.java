package com.toyproject.share_deliveryfee_service.web.partyMessage;

import com.toyproject.share_deliveryfee_service.web.config.ConfigUtil;
import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ProcessingStatus;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfMessage;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.PartyMessageRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import com.toyproject.share_deliveryfee_service.web.payment.PaymentService;
import com.toyproject.share_deliveryfee_service.web.payment.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartyMessageService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final PartyMessageRepository partyMessageRepository;
    private final PaymentsRepository paymentsRepository;

    private final PartyService partyService;
    private final NotificationLogService notificationLogService;
    private final PaymentService paymentService;




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

        // ???????????? ?????? ?????? ?????? ???
        if (choice.equals("yes")){
            if (party.getMembersNum() < party.getMaxMemberNum()) {
                MemberParty memberParty = MemberParty.builder()
                        .member(member)
                        .party(party)
                        .build();

                memberParty.addMemberParty(party, member);
                party.updateMembersNum("up");
                newMessage(party, member, TypeOfMessage.PARTYJOIN, null, 0, 0);
                log.info("?????? ?????? ??????");

                notificationLogService.newNotificationLog(member,
                        "'" + party.getTitle() + "' " + "????????? ?????????????????????.",
                        "/partyDetails/" + party.getId());

                for (MemberParty eachMemberParty : party.getMemberParties()) {
                    if (eachMemberParty.getMember() != member) {
                        notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                                "'" + member.getUsername() + "'?????? '" + party.getTitle() + "' " + "????????????????????? ?????????????????????.",
                                "/partyDetails/" + party.getId());
                    }
                }

                // ????????? ????????? '????????????' ??? ?????? ??????
                if (!(party.getMaxMemberNum() > party.getMembersNum())){
                    partyService.updatePartyStatus(party, "?????? ??????");

                    newMessage(party, member, TypeOfMessage.STATUSCHANGE, null, 0, 0);

                    for (MemberParty eachMemberParty : party.getMemberParties()) {
                        notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                                "'" + party.getTitle() + "' " + "?????? ????????? '?????? ??????'?????? ?????????????????????.",
                                "/partyDetails/" + party.getId());
                    }

                }
            } else {
                log.info("?????? ?????? ??????");
                notificationLogService.newNotificationLog(member,
                        "'" + party.getTitle() + "' " + "?????? ????????? ???????????? ????????? ?????????????????????.",
                        "/partyDetails/" + party.getId());
                return "overcrowding";
            }
        } else if (choice.equals("no")){
            log.info("?????? ?????? ??????");
            paymentService.cancelPayments(paymentsRepository.findByPayerAndCreateAt(member, partyMessage.getCreateAt()));

            notificationLogService.newNotificationLog(member,
                    "'" + party.getTitle() + "' " + "????????????????????? ?????????????????????.",
                    "/partyDetails/" + party.getId());

            notificationLogService.newNotificationLog(party.getOrganizer(),
                    "'" + member.getUsername() + "'?????? '" + party.getTitle() + "' " + "????????????????????? ?????????????????????.",
                    "/partyDetails/" + party.getId());
        }
        return "success";
    }






}
