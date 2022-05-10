package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "notification_logs")
public class NotificationLog extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "notification_log_id")
    private Long id;

    private String contents;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
