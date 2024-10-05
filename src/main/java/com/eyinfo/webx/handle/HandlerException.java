package com.eyinfo.webx.handle;

import com.eyinfo.foundation.CommonException;
import com.eyinfo.foundation.utils.CrashUtils;
import com.eyinfo.foundation.utils.ObjectJudge;
import com.eyinfo.foundation.utils.TextUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public abstract class HandlerException {

    /**
     * 异常消息处理
     *
     * @param code    异常编号
     * @param message 提示消息或异常详情
     * @param trigger true-自定义内部异常消息（可提示）;false-程序详细异常；
     * @return 继承BaseResponse对象
     */
    protected abstract Object onMessageHandler(int code, String message, boolean trigger);

    protected Object onPreHandleException(Exception e) {
        return null;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleException(Exception e) {
        Object preHandleException = onPreHandleException(e);
        if (preHandleException != null) {
            return preHandleException;
        }
        if (e instanceof HttpRequestMethodNotSupportedException exception) {
            String[] methods = exception.getSupportedMethods();
            String message = String.format("接口仅支持【%s】请求", getSupportMethods(methods));
            return onMessageHandler(3014, message, true);
        }
        if (e instanceof HttpMediaTypeNotSupportedException exception) {
            MediaType contentType = exception.getContentType();
            String message = String.format("接口contentType不支持【%s】请求", contentType);
            return onMessageHandler(3015, message, true);
        }
        if (e instanceof MissingServletRequestParameterException msrp) {
            String message = String.format("参数%s缺省", msrp.getParameterName());
            return onMessageHandler(702, message, true);
        }
        if (e instanceof MissingRequestHeaderException mrh) {
            String message = String.format("参数%s缺省", mrh.getHeaderName());
            return onMessageHandler(702, message, true);
        }
        StringBuilder builder = new StringBuilder();
        if (e instanceof BindException be) {
            List<FieldError> errors = be.getFieldErrors();
            for (FieldError error : errors) {
                builder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
            }
            return onMessageHandler(3016, builder.toString(), true);
        }
        if (e instanceof WebExchangeBindException be) {
            List<FieldError> errors = be.getFieldErrors();
            for (FieldError error : errors) {
                builder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
            }
            return onMessageHandler(3016, builder.toString(), true);
        }
        if (e instanceof ConstraintViolationException constraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            int count = constraintViolations.size();
            int position = 0;
            for (ConstraintViolation<?> violation : constraintViolations) {
                Path propertyPath = violation.getPropertyPath();
                String paramName = propertyPath.toString();
                String message = violation.getMessage();
                if (TextUtils.isEmpty(paramName) || message.contains(paramName)) {
                    builder.append(violation.getMessage());
                } else {
                    builder.append(paramName).append(":").append(violation.getMessage());
                }
                if ((position + 1) < count) {
                    builder.append(";");
                }
                position++;
            }
            return onMessageHandler(3017, builder.toString(), true);
        }
        if (e instanceof HttpMessageNotReadableException re) {
            String message = re.getMessage();
            if (!TextUtils.isEmpty(message)) {
                return onMessageHandler(3018, message.split(";")[0], false);
            }
        }
        if (e instanceof CommonException ce) {
            return onMessageHandler(ce.getCode(), CrashUtils.getMessage(ce), false);
        }
        if (e instanceof NoHandlerFoundException foundException) {
            return onMessageHandler(404, foundException.getMessage(), false);
        }
        if (e instanceof InvocationTargetException targetException) {
            Throwable throwable = targetException.getTargetException();
            return onMessageHandler(500, throwable.getMessage(), false);
        }
        String message = e.getMessage();
        if (TextUtils.isEmpty(message)) {
            Throwable throwable = e.getCause();
            return onMessageHandler(400, throwable.getMessage(), false);
        } else {
            return onMessageHandler(400, e.getMessage(), false);
        }
    }

    private String getSupportMethods(String[] methods) {
        if (ObjectJudge.isNullOrEmpty(methods)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String method : methods) {
            builder.append(String.format("%s,", method));
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
