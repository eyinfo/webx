package com.eyinfo.webx.listener;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationVerifyInterceptor {

    boolean onVerify(HttpServletRequest request, String token);
}
