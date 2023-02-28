package com.example.c424.bean.ad;

import com.google.android.gms.ads.nativead.NativeAd;

public class ReportNativeAd {
    private NativeAd nativeAd;
    private long timeStamp;

    public ReportNativeAd() {

    }

    public ReportNativeAd(NativeAd nativeAd, long timeStamp) {
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
        return "ReportNativeAd{" +
                "nativeAd=" + nativeAd +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
