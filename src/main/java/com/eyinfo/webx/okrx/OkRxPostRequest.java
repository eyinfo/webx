package com.eyinfo.webx.okrx;

import com.eyinfo.foundation.utils.ConvertUtils;
import com.eyinfo.foundation.utils.ObjectJudge;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/23
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class OkRxPostRequest extends BaseRequest {

    public void post(OkHttpClient client, String url, String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(responseCallback);
    }

    private final Callback responseCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            onError();
            onCompleted();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ResponseBody body = response.body();
            onSuccessful(body != null ? body.string() : null);
            onCompleted();
        }
    };

    public String postFormAsync(OkHttpClient client, String url, Map<String, String> headers, Map<String, Object> params) {
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
        Request request = builder.url(url).post(formBody).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
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

    public String postFormAsync(OkHttpClient client, String url, Map<String, Object> params) {
        return postFormAsync(client, url, null, params);
    }

    public String postAsync(OkHttpClient client, String url, Map<String, String> headers, String json) {
        String result = "";
        Request.Builder builder = new Request.Builder();
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = builder.url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                result = responseBody.string();
            }
            onSuccessful(result);
        } catch (IOException e) {
            onError();
        }
        onCompleted();
        return result;
    }

    public String postAsync(OkHttpClient client, String url, String json) {
        return postAsync(client, url, null, json);
    }
}
