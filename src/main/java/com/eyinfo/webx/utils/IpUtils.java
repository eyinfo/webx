package com.eyinfo.webx.utils;

import com.eyinfo.foundation.utils.TextUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
    private final String UNKNOWN = "unknown";
    private final String LOCALHOST = "127.0.0.1";
    private final String SEPARATOR = ",";
    //当前主机名
    private String machineHostName = "";

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    //获取本机主机名
    public String getMachineHostName() throws UnknownHostException {
        if (TextUtils.isEmpty(machineHostName)) {
            InetAddress adder = InetAddress.getLocalHost();
            machineHostName = adder.getHostName();
        }
        return machineHostName;
    }
}
