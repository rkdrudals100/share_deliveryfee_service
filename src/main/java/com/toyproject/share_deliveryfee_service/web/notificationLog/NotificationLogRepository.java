package com.toyproject.share_deliveryfee_service.web.notificationLog;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.NotificationLog;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    NotificationLog findNotificationLogById(Long id);

    List<NotificationLog> findByMemberAndReadStatusOrderByCreateAtDesc(Member member, ReadStatus readStatus);

    List<NotificationLog> findByMemberOrderByCreateAtDesc(Member member);
}
