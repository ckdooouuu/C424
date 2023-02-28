package com.example.c424.bean;

public class AdvertisementInfo {
    private int nativeBtn;
    private String adId;
    private int adv_scale;
    private String advFormat;

    public AdvertisementInfo() {

    }

    public AdvertisementInfo(int nativeBtn, String adId, int adv_scale, String advFormat) {
        this.nativeBtn = nativeBtn;
        this.adId = adId;
        this.adv_scale = adv_scale;
        this.advFormat = advFormat;
    }

    public void setNativeBtn(int nativeBtn) {
        this.nativeBtn = nativeBtn;
    }

    public int getNativeBtn() {
        return nativeBtn;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdv_scale(int adv_scale) {
        this.adv_scale = adv_scale;
    }

    public int getAdv_scale() {
        return adv_scale;
    }

    public void setAdvFormat(String advFormat) {
        this.advFormat = advFormat;
    }

    public String getAdvFormat() {
        return advFormat;
    }

    @Override
    public String toString() {
        return "AdInfo{" +
                "nativeBtn=" + nativeBtn +
                ", adId='" + adId + '\'' +
                ", adv_scale=" + adv_scale +
                ", advFormat='" + advFormat + '\'' +
                '}';
    }
}
