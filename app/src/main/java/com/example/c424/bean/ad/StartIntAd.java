package com.example.c424.bean.ad;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class StartIntAd {
    private InterstitialAd interstitialAd;
    private long timeStamp;

    public StartIntAd() {

    }

    public StartIntAd(InterstitialAd interstitialAd, long timeStamp) {
        this.interstitialAd = interstitialAd;
        this.timeStamp = timeStamp;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "StartIntAd{" +
                "interstitialAd=" + interstitialAd +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
