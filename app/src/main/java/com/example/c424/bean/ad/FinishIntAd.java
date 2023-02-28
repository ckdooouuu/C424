package com.example.c424.bean.ad;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class FinishIntAd {
    private InterstitialAd interstitialAd;
    private long timeStamp;

    public FinishIntAd() {

    }

    public FinishIntAd(InterstitialAd interstitialAd, long timeStamp) {
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
        return "FinishIntAd{" +
                "interstitialAd=" + interstitialAd +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
