package com.toyproject.share_deliveryfee_service.web.config.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        log.info("테스트 필터 진입");

//        if(req.getMethod().equals("POST")){
//            log.info("POST 요청됨");
//            String headerAuth = req.getHeader("Authorization");
//            log.info(headerAuth);
//            log.info("테스트 필터 작동");
//
//
//            if(headerAuth.equals("hello")){
//                chain.doFilter(req, res);
//            }else {
//                PrintWriter out = res.getWriter();
//                out.println("Not Authorization");
//            }
//        } else{chain.doFilter(req, res);}
        chain.doFilter(req, res);
    }
}
