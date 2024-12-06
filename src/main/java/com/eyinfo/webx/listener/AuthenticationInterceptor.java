package com.eyinfo.webx.listener;

import com.eyinfo.foundation.annotations.AccessRequired;
import com.eyinfo.foundation.entity.Result;
import com.eyinfo.foundation.enums.VerifyType;
import com.eyinfo.foundation.utils.JsonUtils;
import com.eyinfo.foundation.utils.ObjectJudge;
import com.eyinfo.foundation.utils.TextUtils;
import com.eyinfo.webx.annotations.ConfigurationInterceptor;
import com.eyinfo.webx.store.UserStore;
import com.eyinfo.webx.utils.InjectionUtils;
import com.eyinfo.webx.utils.RequestLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.List;

@ConfigurationInterceptor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private String getUserToken(HttpServletRequest request, VerifyType verify) {
        String token = request.getHeader(UserStore.getInstance().tokenName);
        if (verify == VerifyType.header && !TextUtils.isEmpty(token)) {
            return token;
        }
        String parameter = request.getParameter(UserStore.getInstance().tokenName);
        if (verify == VerifyType.param && !TextUtils.isEmpty(parameter)) {
            return parameter;
        }
        return TextUtils.isEmpty(token) ? parameter : token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse servletResponse, Object handler) throws Exception {
        String token = this.getUserToken(request, VerifyType.both);
        RequestLocalUtils.setToken(token);
        if (!(handler instanceof HandlerMethod)) {
            return true;
        } else {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessRequired access = handlerMethod.getMethodAnnotation(AccessRequired.class);
            if (access != null && access.required()) {
                String verify = request.getHeader("verify");
                List<AuthenticationVerifyInterceptor> authenticationVerifyInterceptors = InjectionUtils.getAuthenticationVerifyInterceptors();
                if (ObjectJudge.isNullOrEmpty(authenticationVerifyInterceptors) || TextUtils.equals(verify, "pass")) {
                    return true;
                }
                boolean flag = true;
                for (AuthenticationVerifyInterceptor authenticationVerifyInterceptor : authenticationVerifyInterceptors) {
                    if (!authenticationVerifyInterceptor.onVerify(request, token)) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    servletResponse.setCharacterEncoding("UTF-8");
                    servletResponse.setContentType("application/json");
                    PrintWriter writer = servletResponse.getWriter();
                    writer.write(JsonUtils.toStr(Result.message("userNoLogin")));
                    writer.flush();
                    writer.close();
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
