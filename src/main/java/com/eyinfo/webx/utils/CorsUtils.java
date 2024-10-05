package com.eyinfo.webx.utils;

import org.springframework.web.servlet.config.annotation.CorsRegistration;

public class CorsUtils {

    // 匹配请求meta属性
    public static CorsRegistration matchGeneralCors(CorsRegistration registration) {
        return registration.allowedOrigins("*")
                //是否发送Cookie信息, allowedOrigins设置*,则allowCredentials不能设置true
                .allowCredentials(false)
                //放行哪些原始域(请求方式)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*")
                //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                .exposedHeaders("content-type", "token", "Authorization");
    }
}
