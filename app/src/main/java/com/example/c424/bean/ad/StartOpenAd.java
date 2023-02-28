package com.example.c424.bean.ad;

import com.google.android.gms.ads.appopen.AppOpenAd;

public class StartOpenAd {
    private AppOpenAd appOpenAd;
    private long timeStamp;

    public StartOpenAd() {

    }

    public StartOpenAd(AppOpenAd appOpenAd, long timeStamp) {
        this.appOpenAd = appOpenAd;
        this.timeStamp = timeStamp;
    }

    public void setAppOpenAd(AppOpenAd appOpenAd) {
        this.appOpenAd = appOpenAd;
    }

    public AppOpenAd getAppOpenAd() {
        return appOpenAd;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "StartOpenAd{" +
                "appOpenAd=" + appOpenAd +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
