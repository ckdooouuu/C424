package com.example.c424.bean;

public class AdInfo {
    private String adId;
    private String location;
    private String type;
    private boolean adSwitch;

    public AdInfo() {

    }

    public AdInfo(String adId, String location, String type, boolean adSwitch) {
        this.adId = adId;
        this.location = location;
        this.type = type;
        this.adSwitch = adSwitch;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAdSwitch() {
        return adSwitch;
    }

    public void setAdSwitch(boolean adSwitch) {
        this.adSwitch = adSwitch;
    }

    @Override
    public String toString() {
        return "AdInfo{" +
                "adId='" + adId + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", adSwitch=" + adSwitch +
                '}';
    }
}
