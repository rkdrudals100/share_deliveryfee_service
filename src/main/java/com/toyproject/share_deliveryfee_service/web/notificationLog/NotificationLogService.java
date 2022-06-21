package com.toyproject.share_deliveryfee_service.web.notificationLog;

import com.toyproject.share_deliveryfee_service.web.config.ConfigUtil;
import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.NotificationLog;
import com.toyproject.share_deliveryfee_service.web.domain.ReadStatus;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.URI;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationLogService {

    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final NotificationLogRepository notificationLogRepository;

    private final ConfigUtil configUtil;

    private String urlPrefix;




    @PostConstruct
    public void init(){
        String databaseProfile = configUtil.getProperty("spring.config.activate.on-profile");

        if (databaseProfile !=null && databaseProfile.equals("prod")){
            this.urlPrefix = "http://3.38.39.166:8080";
        } else {
            this.urlPrefix = "http://localhost:8080";
        }

        log.warn(databaseProfile);
    }



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





    public ReadStatus changeReadStatus(NotificationLog notificationLog, ReadStatus readStatus){
        return notificationLog.updateReadStatus(readStatus);
    }




}
