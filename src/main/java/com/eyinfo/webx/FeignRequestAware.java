package com.eyinfo.webx;

import com.eyinfo.foundation.utils.ConvertUtils;
import com.eyinfo.foundation.utils.TextUtils;
import com.eyinfo.webx.listener.FeignRequestListener;
import com.eyinfo.webx.utils.WebXUtils;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;

public class FeignRequestAware {

    public void apply(RequestTemplate requestTemplate) {
        FeignRequestListener monitor = WebXUtils.getInstance().getFeignRequestMonitor();
        Map<String, Collection<String>> headers = requestTemplate.headers();
        String token = monitor == null ? "" : ConvertUtils.toString(headers.get(monitor.getTokenName()));
        if (TextUtils.isEmpty(token)) {
            if (monitor != null) {
                token = monitor.getAuthorization();
            }
        }
        if (monitor != null) {
            monitor.onApply(requestTemplate, token);
            requestTemplate.header(monitor.getTokenName(), token);
        }
        requestTemplate.header("verify", "pass");
    }
}
