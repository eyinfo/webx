package com.eyinfo.webx.listener;

import com.eyinfo.webx.FeignRequestAware;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        FeignRequestAware requestAware = new FeignRequestAware();
        requestAware.apply(requestTemplate);
    }
}
