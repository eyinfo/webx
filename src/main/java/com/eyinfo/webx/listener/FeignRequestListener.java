package com.eyinfo.webx.listener;

import feign.RequestTemplate;

public interface FeignRequestListener {

    /**
     * 获取token名称
     */
    String getTokenName();

    /**
     * 获取授权值
     */
    String getAuthorization();

    void onApply(RequestTemplate requestTemplate, String token);
}
