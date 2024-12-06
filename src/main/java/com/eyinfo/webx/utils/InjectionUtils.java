package com.eyinfo.webx.utils;

import com.eyinfo.webx.listener.AuthenticationVerifyInterceptor;
import com.eyinfo.webx.listener.InitAwareInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

public class InjectionUtils {

    private static final List<HandlerInterceptor> interceptors = new ArrayList<>();

    private static final List<Runnable> applicationRunners = new ArrayList<>();

    private static List<AuthenticationVerifyInterceptor> authenticationVerifyInterceptors = new ArrayList<>();

    private static List<InitAwareInterceptor> initAwareInterceptors = new ArrayList<>();

    public static List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }

    public static List<Runnable> getApplicationRunners() {
        return applicationRunners;
    }

    public static List<AuthenticationVerifyInterceptor> getAuthenticationVerifyInterceptors() {
        return authenticationVerifyInterceptors;
    }

    public static List<InitAwareInterceptor> getInitAwareInterceptors() {
        return initAwareInterceptors;
    }
}
