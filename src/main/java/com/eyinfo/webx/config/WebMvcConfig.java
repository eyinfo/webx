package com.eyinfo.webx.config;

import com.eyinfo.foundation.utils.ObjectJudge;
import com.eyinfo.webx.utils.CorsUtils;
import com.eyinfo.webx.utils.InjectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsUtils.matchGeneralCors(registry.addMapping("/**"));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<HandlerInterceptor> interceptors = InjectionUtils.getInterceptors();
        if (!ObjectJudge.isNullOrEmpty(interceptors)) {
            for (HandlerInterceptor interceptor : interceptors) {
                registry.addInterceptor(interceptor);
            }
        }
    }
}
