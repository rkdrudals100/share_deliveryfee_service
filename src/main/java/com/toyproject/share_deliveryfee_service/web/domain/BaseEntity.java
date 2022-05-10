package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//  2022-04-14 강경민
//  도메인 설계
@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedBy
    private LocalDateTime updateAt;
}
