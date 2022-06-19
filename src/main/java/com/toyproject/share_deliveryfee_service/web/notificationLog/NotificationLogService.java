package com.toyproject.share_deliveryfee_service.web.notificationLog;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.NotificationLog;
import com.toyproject.share_deliveryfee_service.web.domain.ReadStatus;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationLogService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final NotificationLogRepository notificationLogRepository;

    String urlPrefix = "http://localhost:8080";






    public void newNotificationLog(Member member, String contents, String url){

        NotificationLog notificationLog = NotificationLog.builder()
                .member(member)
                .contents(contents)
                .url(urlPrefix + url)
                .readStatus(ReadStatus.NOTREAD)
                .build();

        notificationLogRepository.save(notificationLog);

//        member.addNotificationLogs(notificationLog);
    }




}
