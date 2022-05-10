package com.toyproject.share_deliveryfee_service.web.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Map<String, String> DupCheckByUserId(String userId, Map<String, String> returnMap){

        returnMap.put("beforeMemberId", userId);

        if (memberRepository.findByUsername(userId) == null){
            returnMap.put("dupCheckId", "true");
            log.info("'{}' 아이디 중복 체크, 중복 없음", userId);
        } else {
            returnMap.put("dupCheckId", "false");
            log.info("'{}' 아이디 중복 체크, 중복 발생", userId);
        }

        return returnMap;
    }
}
