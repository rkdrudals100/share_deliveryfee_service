package com.toyproject.share_deliveryfee_service.web.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.share_deliveryfee_service.web.config.auth.PrincipalDetails;
import com.toyproject.share_deliveryfee_service.web.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도중");

        try {
            BufferedReader br = request.getReader();

            String input = br.readLine();

            log.info(input);
            String[] pm = input.split("&");

            String username = pm[0];
            String password = pm[1];

            Member member = Member.builder()
                    .username(username.replace("username=", ""))
                    .password(password.replace("password=", ""))
                    .build();


//            ObjectMapper om = new ObjectMapper();
//            Member member = om.readValue(request.getInputStream(), Member.class);
//            log.info(String.valueOf(member));
//            log.info("json 받아서 저장 완료");


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            log.info("토큰 생성 완료");
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            log.info("로그인 됨");
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info(principalDetails.getMember().getUsername());
            log.info("authentication 생성 완료");

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
//            return null;
        }
        return null;
    }




    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("로그인 성공로직 진입");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();


        String jwtToken = JWT.create()
                .withSubject("jwtLoginToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 10)))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getUsername())
                .sign(Algorithm.HMAC512("kkm"));


        response.addHeader("Authorization", "Bearer " + jwtToken);
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}










