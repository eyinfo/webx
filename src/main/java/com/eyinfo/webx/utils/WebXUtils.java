package com.eyinfo.webx.utils;

import com.eyinfo.webx.listener.FeignRequestListener;

public class WebXUtils {
    private static WebXUtils webXUtils;

    private WebXUtils() {
    }

    public static WebXUtils getInstance() {
        if (webXUtils == null) {
            synchronized (WebXUtils.class) {
                if (webXUtils == null) {
                    webXUtils = new WebXUtils();
                }
            }
        }
        return webXUtils;
    }

    private static FeignRequestListener feignRequestListener;

    public <L extends FeignRequestListener> void registerFeignRequestMonitor(L listener) {
        feignRequestListener = listener;
    }

    public FeignRequestListener getFeignRequestMonitor() {
        return feignRequestListener;
    }
}
