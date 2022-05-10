package com.toyproject.share_deliveryfee_service.web.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.toyproject.share_deliveryfee_service.web.config.auth.PrincipalDetails;
import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인증이나 권한이 필요한 주소 요청 발생");

        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader: " + jwtHeader);

        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            log.info("서명 정상적으로 실행 XXXX");
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC512("kkm")).build().verify(jwtToken).getClaim("username").asString();
        log.info("username: " + username);

        // 서명이 정상적으로 되었을 경우
        if (username != null){
            Member memberEntity = memberRepository.findByUsername(username);
            log.info("서명 정상적으로 실행");
            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            log.info("서명 정상적으로 실행2");
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            log.info("서명 정상적으로 실행3");
            // 시큐리티의 세션에 접근해서 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("서명 정상적으로 실행4");
            chain.doFilter(request, response);
        }
    }
}
