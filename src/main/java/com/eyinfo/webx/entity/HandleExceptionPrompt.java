package com.eyinfo.webx.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "exception")
public class HandleExceptionPrompt implements Serializable {

    //接口支持的请求方式
    private String supportMethods = "接口仅支持【%s】请求";

    //请求参数类型不支持
    private String contentTypeNotSupported = "接口contentType不支持【%s】请求";

    //参数缺省
    private String parameterMissing = "参数%s缺省";

    //请求体缺省
    private String bodyMissing = "缺少请求体数据";

    public String getSupportMethods() {
        return supportMethods;
    }

    public void setSupportMethods(String supportMethods) {
        this.supportMethods = supportMethods;
    }

    public String getContentTypeNotSupported() {
        return contentTypeNotSupported;
    }

    public void setContentTypeNotSupported(String contentTypeNotSupported) {
        this.contentTypeNotSupported = contentTypeNotSupported;
    }

    public String getParameterMissing() {
        return parameterMissing;
    }

    public void setParameterMissing(String parameterMissing) {
        this.parameterMissing = parameterMissing;
    }

    public String getBodyMissing() {
        return bodyMissing;
    }

    public void setBodyMissing(String bodyMissing) {
        this.bodyMissing = bodyMissing;
    }
}
