package com.eyinfo.webx.okrx;

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
public class OkRxGetRequest extends BaseRequest {

    public void get(OkHttpClient client, String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        client.newCall(request).enqueue(responseCallback);
    }

    public void get(OkHttpClient client, String url) {
        get(client, url, null);
    }

    private final Callback responseCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            onError();
            onCompleted();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            ResponseBody responseBody = response.body();
            if (responseBody!=null) {
                String body = responseBody.string();
                onSuccessful(body);
            }
            onCompleted();
        }
    };

    public String getAsync(OkHttpClient client, String url, Map<String, String> headers) {
        String result = "";
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (!ObjectJudge.isNullOrEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
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

    public String getAsync(OkHttpClient client, String url) {
        return getAsync(client, url, null);
    }
}
