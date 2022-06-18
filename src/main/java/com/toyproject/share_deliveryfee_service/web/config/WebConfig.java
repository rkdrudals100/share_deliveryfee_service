package com.toyproject.share_deliveryfee_service.web.config;

import com.toyproject.share_deliveryfee_service.web.config.interceptor.NotificationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NotificationInterceptor notificationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(notificationInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/favicon.ico");
    }
}
