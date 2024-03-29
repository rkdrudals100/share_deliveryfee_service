package com.toyproject.share_deliveryfee_service.web.notificationLog;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.NotificationLog;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReadStatus;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationLogController {

    private final MemberRepository memberRepository;
    private final NotificationLogRepository notificationLogRepository;

    private final NotificationLogService notificationLogService;




    @PatchMapping("/userInfo/selectedTab/notification/notificationLog/{notificationLogId}")
    @ResponseBody
    public Map<String, String> alertClicked(@RequestBody Map<String, Object> inputMap){

        Map<String, String> returnMap = new HashMap<>();
        String notificationLogId = (String)inputMap.get("getNotificationLogId");

        notificationLogService.changeReadStatus(notificationLogRepository.findNotificationLogById(Long.parseLong(notificationLogId)), ReadStatus.READ);

        return returnMap;
    }



    @PatchMapping("/userInfo/selectedTab/notification/notificationLog")
    public String readStatusAllChange(Principal principal){

        Member member = memberRepository.findByUsername(principal.getName());

        for(NotificationLog notificationLog: notificationLogRepository.findByMemberOrderByCreateAtDesc(member)){
            notificationLogService.changeReadStatus(notificationLog, ReadStatus.READ);
        }
        return "redirect:/accountInfo/selectedTab/notification";
    }
}
