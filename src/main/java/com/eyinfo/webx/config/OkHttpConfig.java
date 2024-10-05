package com.eyinfo.webx.config;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpConfig {

    private static OkHttpConfig httpConfig;
    private OkHttpClient client;
    private OkHttpClient.Builder builder;

    public static OkHttpConfig getInstance() {
        if (httpConfig == null) {
            synchronized (OkHttpConfig.class) {
                if (httpConfig == null) {
                    httpConfig = new OkHttpConfig();
                }
            }
        }
        return httpConfig;
    }

    private OkHttpConfig() {

    }

    public OkHttpClient.Builder getBuilder() {
        if (builder == null) {
            builder = new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS);
        }
        return builder;
    }

    public OkHttpClient getClient() {
        if (client == null) {
            client = getBuilder().build();
        }
        return client;
    }
}
