package com.eyinfo.webx.utils;

import com.eyinfo.foundation.enums.VerifyType;
import com.eyinfo.foundation.events.Func2;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

public class InjectionUtils {

    private static final List<HandlerInterceptor> interceptors = new ArrayList<>();

    private static final List<Runnable> applicationRunners = new ArrayList<>();

    private static Func2<Boolean, HttpServletRequest, VerifyType> authenticationVerifyFunc = null;

    public static List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }

    public static List<Runnable> getApplicationRunners() {
        return applicationRunners;
    }

    public static void setAuthenticationVerifyFunc(Func2<Boolean, HttpServletRequest, VerifyType> authenticationVerifyFunc) {
        InjectionUtils.authenticationVerifyFunc = authenticationVerifyFunc;
    }

    public static Func2<Boolean, HttpServletRequest, VerifyType> getAuthenticationVerifyFunc() {
        return authenticationVerifyFunc;
    }
}
