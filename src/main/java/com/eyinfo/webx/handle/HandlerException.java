package com.eyinfo.webx.handle;

import com.eyinfo.foundation.CommonException;
import com.eyinfo.foundation.entity.BaseResponse;
import com.eyinfo.foundation.entity.Result;
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
     * 异常消息回调
     *
     * @param code    异常编号
     * @param message 提示消息或异常详情
     */
    public void onError(int code, String message) {
        // 记录异常信息
    }

    protected Object onPreHandleException(Exception e) {
        return null;
    }

    private BaseResponse getExceptionMessage(String messageKey, String replace) {
        BaseResponse message = Result.message(messageKey);
        if (!TextUtils.isEmpty(replace)) {
            message.setMsg(String.format(message.getMsg(), replace));
        }
        return message;
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
            return getExceptionMessage("supportMethods", getSupportMethods(methods));
        }
        if (e instanceof HttpMediaTypeNotSupportedException exception) {
            MediaType contentType = exception.getContentType();
            return getExceptionMessage("contentTypeNotSupported", contentType != null ? contentType.toString() : null);
        }
        if (e instanceof MissingServletRequestParameterException msrp) {
            return getExceptionMessage("parameterMissing", msrp.getParameterName());
        }
        if (e instanceof MissingRequestHeaderException mrh) {
            return getExceptionMessage("parameterMissing", mrh.getHeaderName());
        }
        StringBuilder builder = new StringBuilder();
        if (e instanceof BindException be) {
            List<FieldError> errors = be.getFieldErrors();
            for (FieldError error : errors) {
                builder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
            }
            BaseResponse response = Result.message("globalServerError");
            onError(response.getCode(), builder.toString());
            return response;
        }
        if (e instanceof WebExchangeBindException be) {
            List<FieldError> errors = be.getFieldErrors();
            for (FieldError error : errors) {
                builder.append(error.getField()).append(":").append(error.getDefaultMessage()).append(";");
            }
            BaseResponse response = Result.message("globalServerError");
            onError(response.getCode(), builder.toString());
            return response;
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
            BaseResponse response = Result.message("globalServerError");
            onError(response.getCode(), builder.toString());
            return response;
        }
        if (e instanceof HttpMessageNotReadableException re) {
            return getExceptionMessage("bodyMissing", null);
        }
        if (e instanceof CommonException ce) {
            onError(ce.getCode(), CrashUtils.getMessage(ce));
            return Result.message("globalServerError");
        }
        if (e instanceof NoHandlerFoundException foundException) {
            onError(404, foundException.getMessage());
            return Result.message("globalServerError");
        }
        if (e instanceof InvocationTargetException targetException) {
            Throwable throwable = targetException.getTargetException();
            onError(500, throwable.getMessage());
            return Result.message("globalServerError");
        }
        String message = e.getMessage();
        if (TextUtils.isEmpty(message)) {
            Throwable throwable = e.getCause();
            message = throwable.getMessage();
        }
        BaseResponse response = Result.message("globalServerError");
        onError(response.getCode(), message);
        return response;
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
