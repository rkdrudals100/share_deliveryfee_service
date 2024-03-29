package com.toyproject.share_deliveryfee_service.web.config.interceptor;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.NotificationLog;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReadStatus;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final NotificationLogRepository notificationLogRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        // 사용자 정보 받아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 알림이 필요한 페이지, 사용자라고 판별될 경우 modelAndView에 읽지 않은 알림정보를 담아서 전송
        if (modelAndView != null && !isRedirectView(modelAndView) && authentication != null && authentication.getPrincipal() instanceof UserDetails){

            Member member = memberRepository.findByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
            List<NotificationLog> notificationLogs = notificationLogRepository.findByMemberAndReadStatusOrderByCreateAtDesc(member, ReadStatus.NOTREAD);
            modelAndView.addObject("navNotificationLogs", notificationLogs);
        }

    }

    private boolean isRedirectView(ModelAndView modelAndView){
        return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
    }
}
