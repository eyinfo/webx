package com.eyinfo.webx;

import com.eyinfo.foundation.entity.Result;
import com.eyinfo.webx.handle.DefaultMessageHandle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class WebXConfigurationAware implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        DefaultMessageHandle messageHandle = new DefaultMessageHandle();
        Result.setMessageConfig(messageHandle.getDefaultMessageHandle());
    }
}
