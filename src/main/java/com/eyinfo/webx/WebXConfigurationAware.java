package com.eyinfo.webx;

import com.eyinfo.foundation.entity.Result;
import com.eyinfo.webx.handle.DefaultMessageHandle;
import com.eyinfo.webx.listener.InitAwareInterceptor;
import com.eyinfo.webx.utils.InjectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebXConfigurationAware implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        DefaultMessageHandle messageHandle = new DefaultMessageHandle();
        Result.setMessageConfig(messageHandle.getDefaultMessageHandle());
        //调用Aware注入类
        List<InitAwareInterceptor> awareInterceptors = InjectionUtils.getInitAwareInterceptors();
        for (InitAwareInterceptor awareInterceptor : awareInterceptors) {
            awareInterceptor.onAware(applicationContext);
        }
    }
}
