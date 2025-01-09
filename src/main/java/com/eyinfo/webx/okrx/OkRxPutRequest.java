package com.eyinfo.webx.okrx;

import com.eyinfo.foundation.utils.ConvertUtils;
import com.eyinfo.foundation.utils.ObjectJudge;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkRxPutRequest extends BaseRequest {

    public String putAsync(OkHttpClient client, String url, Map<String, String> headers, Map<String, Object> params) {
        String result = "";
        Request.Builder builder = new Request.Builder();
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (!ObjectJudge.isNullOrEmpty(params)) {
            for (String key : params.keySet()) {
                bodyBuilder.add(key, ConvertUtils.toString(params.get(key)));
            }
        }
        RequestBody formBody = bodyBuilder.build();
        Request request = builder.url(url).put(formBody).build();
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
}
