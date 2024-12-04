package com.eyinfo.webx.entity;

import java.io.Serializable;

public class RequestInfo implements Serializable {

    //用户token
    private String token;

    //请求用户ip
    private String ip;

    //请求设备id
    private String deviceId;

    //1-android; 2-ios;
    private String platform;

    //渠道
    private String channel;

    //版本
    private String version;

    //语言
    private String language;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
