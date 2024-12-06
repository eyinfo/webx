package com.eyinfo.webx;

import com.eyinfo.foundation.utils.ObjectJudge;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AOPSectionAspect {

    public void executeService() {
        //服务调用开始处理逻辑
    }

    protected void onApiStartAdvice(Method method, Map<String, Object> paramMap) {
        //接口调用开始处理逻辑
    }

    protected void onApiEndAdvice(Method method, Map<String, Object> paramMap, Object result) {
        //接口调用结束处理逻辑
    }

    @Around("executeService()")
    protected Object onServiceAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        this.executeService();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            //非http请求、多线程环境、请求结束后或spring配置问题均
            return proceedingJoinPoint.proceed();
        }
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method methodObject = methodSignature.getMethod();
        HttpServletRequest request = attributes.getRequest();
        Map<String, Object> paramMap = processInputArg(request, methodSignature, proceedingJoinPoint.getArgs());
        onApiStartAdvice(methodObject, paramMap);
        Object proceed = proceedingJoinPoint.proceed();
        onApiEndAdvice(methodObject, paramMap, proceed);
        return proceed;
    }

    private Map<String, Object> processInputArg(HttpServletRequest request, MethodSignature methodSignature, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        String[] parameterNames = methodSignature.getParameterNames();
        if (!ObjectJudge.isNullOrEmpty(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                Object parameterValue = args[i];
                if (parameterValue instanceof Integer || parameterValue instanceof Long || parameterValue instanceof Double || parameterValue instanceof String) {
                    map.put(parameterName, parameterValue);
                }
            }
        }
        return map;
    }

    @After("executeService()")
    public void onServiceAfterAdvice() {
        //服务调用结束处理逻辑
    }
}
