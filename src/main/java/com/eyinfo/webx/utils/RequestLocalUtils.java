package com.eyinfo.webx.utils;

import com.eyinfo.webx.entity.RequestInfo;
import feign.RequestTemplate;

public class RequestLocalUtils {

    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        RequestInfo requestInfo = threadLocal.get();
        return requestInfo == null ? new RequestInfo() : requestInfo;
    }

    public static void setToken(String token) {
        RequestInfo requestInfo = get();
        requestInfo.setToken(token);
        threadLocal.set(requestInfo);
    }

    public static String getToken() {
        RequestInfo requestInfo = get();
        return requestInfo.getToken();
    }

    public static String getPlatform() {
        RequestInfo requestInfo = get();
        return requestInfo.getPlatform();
    }

    public static void setPlatform(String platform) {
        RequestInfo requestInfo = get();
        requestInfo.setPlatform(platform);
        threadLocal.set(requestInfo);
    }

    public static String getIp() {
        RequestInfo requestInfo = get();
        return requestInfo.getIp();
    }

    public static void setIp(String ip) {
        RequestInfo requestInfo = get();
        requestInfo.setIp(ip);
        threadLocal.set(requestInfo);
    }

    public static void setChannel(String channel) {
        RequestInfo requestInfo = get();
        requestInfo.setChannel(channel);
        threadLocal.set(requestInfo);
    }

    public static String getChannel() {
        RequestInfo requestInfo = get();
        return requestInfo.getChannel();
    }

    public static String getDeviceId() {
        RequestInfo requestInfo = get();
        return requestInfo.getDeviceId();
    }

    public static void setDeviceId(String deviceId) {
        RequestInfo requestInfo = get();
        requestInfo.setDeviceId(deviceId);
        threadLocal.set(requestInfo);
    }

    public static String getVersion() {
        RequestInfo requestInfo = get();
        return requestInfo.getVersion();
    }

    public static void setVersion(String version) {
        RequestInfo requestInfo = get();
        requestInfo.setVersion(version);
        threadLocal.set(requestInfo);
    }

    public static String getLanguage() {
        RequestInfo requestInfo = get();
        return requestInfo.getLanguage();
    }

    public static void setLanguage(String language) {
        RequestInfo requestInfo = get();
        requestInfo.setLanguage(language);
        threadLocal.set(requestInfo);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void setRequestTemplateHeader(RequestTemplate requestTemplate) {
        RequestInfo requestInfo = get();
        requestTemplate.header("version", requestInfo.getVersion());
        requestTemplate.header("language", requestInfo.getLanguage());
        requestTemplate.header("token", requestInfo.getToken());
        requestTemplate.header("channel", requestInfo.getChannel());
        requestTemplate.header("platform", requestInfo.getPlatform());
        requestTemplate.header("ip", requestInfo.getIp());
        requestTemplate.header("deviceId", requestInfo.getDeviceId());
    }
}
