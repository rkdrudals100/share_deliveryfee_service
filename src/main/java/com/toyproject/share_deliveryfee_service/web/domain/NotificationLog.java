package com.toyproject.share_deliveryfee_service.web.domain;

import com.toyproject.share_deliveryfee_service.web.domain.enums.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "notification_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "notification_log_id")
    private Long id;

    private String contents;

    private String url;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;




    // 필드 변경 메소드
    public ReadStatus updateReadStatus(ReadStatus readStatus){
        this.readStatus = readStatus;

        return this.readStatus;
    }
}
