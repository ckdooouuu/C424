package com.example.c424.bean.ad;

import com.google.android.gms.ads.nativead.NativeAd;

import java.lang.annotation.Native;

public class HomeNativeAd {
    private NativeAd nativeAd;
    private long timeStamp;

    public HomeNativeAd() {

    }

    public HomeNativeAd(NativeAd nativeAd, long timeStamp) {
        this.nativeAd = nativeAd;
        this.timeStamp = timeStamp;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "HomeNativeAd{" +
                "nativeAd=" + nativeAd +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
