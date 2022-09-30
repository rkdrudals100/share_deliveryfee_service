package com.toyproject.share_deliveryfee_service.web.scheduleService;

import com.toyproject.share_deliveryfee_service.web.domain.MemberParty;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfMessage;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import com.toyproject.share_deliveryfee_service.web.partyMessage.PartyMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class timeScheduler {

    private final PartyRepository partyRepository;

    private final PartyService partyService;
    private final PartyMessageService partyMessageService;
    private final NotificationLogService notificationLogService;




    // 예약 실행 메소드
    @Scheduled(fixedDelay = 1000 * 60)
    public void checkPartyLimitTime(){

        // 모집 시간이 끝난 모집글 탐색
        List<Party> parties = partyRepository.findByLimitTimeBeforeAndPartyStatus(LocalDateTime.now(), PartyStatus.RECRUITING);

        // 모집 완료로 파티 상태 변경
        for (Party eachParty: parties){
            partyService.updatePartyStatus(eachParty, "모집 완료");

            // 파티 상태가 변경되었다는 파티 메시지와 알림 생성
            partyMessageService.newMessage(eachParty, eachParty.getOrganizer(), TypeOfMessage.STATUSCHANGE,
                    null, 0, 0);

            for (MemberParty eachMemberParty : eachParty.getMemberParties()) {
                notificationLogService.newNotificationLog(eachMemberParty.getMember(),
                        "모집시간이 끝나서 '" + eachParty.getTitle() + "' " + "파티 상태가 '모집 완료' 로 변경되었습니다.",
                        "/partyDetails/" + eachParty.getId());
            }
        }
    }
}
