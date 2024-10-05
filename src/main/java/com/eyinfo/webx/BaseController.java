package com.eyinfo.webx;


import com.eyinfo.foundation.entity.BaseResponse;
import com.eyinfo.webx.annotations.GSModuleUnique;
import com.eyinfo.webx.entity.ValidReturn;
import com.eyinfo.webx.handle.HeadersEntry;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class BaseController {

    /**
     * 验证是否通过
     *
     * @param result 验证结果
     * @return
     */
    private ValidReturn bindingCheck(BindingResult result) {
        ValidReturn validReturn = new ValidReturn();
        BaseResponse response = new BaseResponse();
        if (result == null) {
            validReturn.setPassable(false);
            response.setCode(100);
        } else {
            if (result.hasErrors()) {
                validReturn.setPassable(false);
                response.setCode(700);
                StringBuilder builder = new StringBuilder();
                List<ObjectError> allErrors = result.getAllErrors();
                for (ObjectError error : allErrors) {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        builder.append(fieldError.getField()).append(":").append(error.getDefaultMessage()).append(";");
                    }
                }
                response.setMsg(builder.toString());
            } else {
                validReturn.setPassable(true);
                response.setCode(200);
            }
        }
        validReturn.setValidResponse(response);
        return validReturn;
    }

    protected <T> Object valid(BindingResult result, T arguments) {
        ValidReturn check = bindingCheck(result);
        if (!check.isPassable()) {
            return check.getValidResponse();
        }
        return arguments;
    }

    /**
     * 获取模块标识
     *
     * @return
     */
    protected String getModuleUnique() {
        Class<? extends BaseController> aClass = this.getClass();
        if (!aClass.isAnnotationPresent(GSModuleUnique.class)) {
            return "";
        }
        GSModuleUnique annotation = aClass.getAnnotation(GSModuleUnique.class);
        return annotation.unique();
    }

    /**
     * 获取请求头对象
     *
     * @param headers http headers
     * @return HeadersEntrys
     */
    protected HeadersEntry getHeaders(HttpHeaders headers) {
        HeadersEntry entry = new HeadersEntry();
        if (headers == null || headers.isEmpty()) {
            return entry;
        }
        entry.setHeaders(headers);
        return entry;
    }
}
