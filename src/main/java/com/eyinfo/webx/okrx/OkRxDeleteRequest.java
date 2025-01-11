package com.eyinfo.webx.okrx;

import com.eyinfo.foundation.utils.ConvertUtils;
import com.eyinfo.foundation.utils.JsonUtils;
import com.eyinfo.foundation.utils.ObjectJudge;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkRxDeleteRequest extends BaseRequest {

    public String deleteAsync(OkHttpClient client, String url, Map<String, String> headers, Map<String, Object> params, boolean isJson) {
        String result = "";
        Request.Builder builder = new Request.Builder();
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = null;
        if (isJson) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = JsonUtils.toStr(params);
            requestBody = RequestBody.create(JSON, json);
        } else {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (!ObjectJudge.isNullOrEmpty(params)) {
                for (String key : params.keySet()) {
                    bodyBuilder.add(key, ConvertUtils.toString(params.get(key)));
                }
            }
            requestBody = bodyBuilder.build();
        }
        Request request = builder.url(url).delete(requestBody).build();
        ResponseBody body;
        try (Response response = client.newCall(request).execute()) {
            body = response.body();
            if (body != null) {
                result = body.string();
            }
            onSuccessful(result);
        } catch (IOException e) {
            onError();
        }
        onCompleted();
        return result;
    }

    public String deleteAsync(OkHttpClient client, String url, Map<String, String> headers, Map<String, Object> params) {
        return deleteAsync(client, url, headers, params, false);
    }

    public String deleteAsync(OkHttpClient client, String url, Map<String, Object> params, boolean isJson) {
        return deleteAsync(client, url, null, params, isJson);
    }

    public String deleteAsync(OkHttpClient client, String url, Map<String, Object> params) {
        return deleteAsync(client, url, null, params, false);
    }
}
