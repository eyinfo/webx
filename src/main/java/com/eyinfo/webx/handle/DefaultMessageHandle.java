package com.eyinfo.webx.handle;

import com.eyinfo.foundation.entity.BaseResponse;

import java.util.HashMap;
import java.util.Map;

public class DefaultMessageHandle {

    public Map<String, BaseResponse> getDefaultMessageHandle() {
        Map<String, BaseResponse> map = new HashMap<>();
        map.put("supportMethods", new BaseResponse(20000, "接口仅支持[%s]请求"));
        map.put("contentTypeNotSupported", new BaseResponse(20001, "接口ContentType不支持[%s]请求"));
        map.put("parameterMissing", new BaseResponse(20002, "参数%s缺省"));
        map.put("bodyMissing", new BaseResponse(20003, "请求体数据缺失"));
        map.put("parameterEmpty", new BaseResponse(20004, "请求参数为空"));
        map.put("globalServerError", new BaseResponse(20005, "出现了一个问题，我们正在解决。"));
        map.put("apiCallLimit", new BaseResponse(20006, "API调用频率受限"));
        map.put("serviceCloseLimit", new BaseResponse(20007, "供应商关闭了你的授权"));
        map.put("routeForwardEmpty", new BaseResponse(20008, "转发地址为空"));
        map.put("routeIdEmpty", new BaseResponse(20009, "路由id为空"));
        map.put("routeAssertNotConfig", new BaseResponse(20010, "没有配置路由断言"));
        map.put("apiAccountLose", new BaseResponse(20011, "appId或secret无效"));
        map.put("routeFilterNotConfig", new BaseResponse(20012, "没有配置路由过滤器"));
        return map;
    }
}
