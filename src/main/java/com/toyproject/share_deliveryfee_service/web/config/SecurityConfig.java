package com.toyproject.share_deliveryfee_service.web.config;

import com.toyproject.share_deliveryfee_service.web.config.filter.MyFilter;
import com.toyproject.share_deliveryfee_service.web.config.jwt.JwtAuthenticationFilter;
import com.toyproject.share_deliveryfee_service.web.config.jwt.JwtAuthorizationFilter;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final MemberRepository memberRepository;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/login", "/signUp/**", "/index", "/testdatasave", "/img/**", "/js/**", "/css/**", "/token", "/").permitAll()
                .anyRequest().authenticated();




//        http.formLogin().disable();




//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic();
        http.csrf().disable();
//        http.httpBasic().disable();
//
//        http.addFilter(corsFilter);
//        http.addFilterBefore(new MyFilter(), SecurityContextPersistenceFilter.class);
//        http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
//        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository));

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/")
                .defaultSuccessUrl("/");
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
}
