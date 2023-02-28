package com.example.c424.bean;

import java.util.List;

public class AdConfig {
    private String adLocation;
    private List<AdvertisementInfo> innerAd;
    private boolean closeAdv;

    public AdConfig() {

    }

    public AdConfig(String adLocation, List<AdvertisementInfo> innerAd, boolean closeAdv) {
        this.adLocation = adLocation;
        this.innerAd = innerAd;
        this.closeAdv = closeAdv;
    }

    public String getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(String adLocation) {
        this.adLocation = adLocation;
    }

    public List<AdvertisementInfo> getInnerAd() {
        return innerAd;
    }

    public void setInnerAd(List<AdvertisementInfo> innerAd) {
        this.innerAd = innerAd;
    }

    public boolean isCloseAdv() {
        return closeAdv;
    }

    public void setCloseAdv(boolean closeAdv) {
        this.closeAdv = closeAdv;
    }

    @Override
    public String toString() {
        return "AdConfig{" +
                "adLocation='" + adLocation + '\'' +
                ", innerAd=" + innerAd +
                ", closeAdv=" + closeAdv +
                '}';
    }
}
