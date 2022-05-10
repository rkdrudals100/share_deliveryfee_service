package com.toyproject.share_deliveryfee_service.web.member;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String userId);

    Member findByPhoneNum(String phoneNum);
}
