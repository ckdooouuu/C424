package com.example.c424.bean;

public class ReqUploadConnectionResult extends BaseReqBean {
    private String userIp;
    private ConnectionProxyInfo serverInfo;

    public ReqUploadConnectionResult() {

    }

    public ReqUploadConnectionResult(String projectName, int editionId, String deviceId, String adid_fa, int sdkCode,
                              String languages, String appCountry, long nowTime, String publicNetwork,
                              String phoneSim, long openAppTime, String userIp, ConnectionProxyInfo serverInfo) {
        super(projectName, editionId, deviceId, adid_fa, sdkCode, languages, appCountry, nowTime, publicNetwork, phoneSim, openAppTime);
        this.userIp = userIp;
        this.serverInfo = serverInfo;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setServerInfo(ConnectionProxyInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ConnectionProxyInfo getServerInfo() {
        return serverInfo;
    }

    @Override
    public String toString() {
        return "ReqUploadConnectionResult{" +
                "userIp='" + userIp + '\'' +
                ", serverInfo=" + serverInfo +
                '}';
    }
}
