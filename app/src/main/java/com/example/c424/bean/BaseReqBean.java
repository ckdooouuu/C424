package com.example.c424.bean;

public class BaseReqBean {
    private String projectName;
    private int editionId;
    private String deviceId;
    private String adid_fa;
    private int sdkCode;
    private String languages;
    private String appCountry;
    private long nowTime;
    private String publicNetwork;
    private String phoneSim;
    private long openAppTime;

    public BaseReqBean() {

    }

    public BaseReqBean(String projectName, int editionId, String deviceId, String adid_fa, int sdkCode,
                       String languages, String appCountry, long nowTime, String publicNetwork,
                       String phoneSim, long openAppTime) {
        this.projectName = projectName;
        this.editionId = editionId;
        this.deviceId = deviceId;
        this.adid_fa = adid_fa;
        this.sdkCode = sdkCode;
        this.languages = languages;
        this.appCountry = appCountry;
        this.nowTime = nowTime;
        this.publicNetwork = publicNetwork;
        this.phoneSim = phoneSim;
        this.openAppTime = openAppTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getEditionId() {
        return editionId;
    }

    public void setEditionId(int editionId) {
        this.editionId = editionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAdid_fa() {
        return adid_fa;
    }

    public void setAdid_fa(String adid_fa) {
        this.adid_fa = adid_fa;
    }

    public int getSdkCode() {
        return sdkCode;
    }

    public void setSdkCode(int sdkCode) {
        this.sdkCode = sdkCode;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAppCountry() {
        return appCountry;
    }

    public void setAppCountry(String appCountry) {
        this.appCountry = appCountry;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public String getPublicNetwork() {
        return publicNetwork;
    }

    public void setPublicNetwork(String publicNetwork) {
        this.publicNetwork = publicNetwork;
    }

    public String getPhoneSim() {
        return phoneSim;
    }

    public void setPhoneSim(String phoneSim) {
        this.phoneSim = phoneSim;
    }

    public long getOpenAppTime() {
        return openAppTime;
    }

    public void setOpenAppTime(long openAppTime) {
        this.openAppTime = openAppTime;
    }

    @Override
    public String toString() {
        return "BaseReqBean{" +
                "projectName='" + projectName + '\'' +
                ", editionId=" + editionId +
                ", deviceId='" + deviceId + '\'' +
                ", adid_fa='" + adid_fa + '\'' +
                ", sdkCode='" + sdkCode + '\'' +
                ", languages='" + languages + '\'' +
                ", appCountry='" + appCountry + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", publicNetwork='" + publicNetwork + '\'' +
                ", phoneSim='" + phoneSim + '\'' +
                ", openAppTime=" + openAppTime +
                '}';
    }
}
