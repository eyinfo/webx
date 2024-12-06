package com.eyinfo.webx.listener;

import org.springframework.context.ApplicationContext;

public interface InitAwareInterceptor {
    void onAware(ApplicationContext applicationContext);
}
