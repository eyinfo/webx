package com.eyinfo.webx.handle;

import com.eyinfo.foundation.utils.ConvertUtils;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/27
 * Description:请求头对象
 * Modifier:
 * ModifyContent:
 */
public class HeadersEntry {

    private HttpHeaders headers;

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    /**
     * 获取属性值
     *
     * @param key 键
     * @return header info
     */
    public String getValue(String key) {
        if (headers == null || !headers.containsKey(key)) {
            return "";
        }
        List<String> params = headers.get(key);
        if (params == null || params.size() == 0) {
            return "";
        }
        return params.get(0);
    }

    /**
     * 获取头int值
     *
     * @param key 键
     * @return header info
     */
    public int getIntValue(String key) {
        String value = getValue(key);
        return ConvertUtils.toInt(value);
    }
}
