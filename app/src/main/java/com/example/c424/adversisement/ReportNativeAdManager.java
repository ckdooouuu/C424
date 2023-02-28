package com.example.c424.adversisement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.act.LocationActivity;
import com.example.c424.act.MainActivity;
import com.example.c424.act.ReportActivity;
import com.example.c424.act.SpeedActivity;
import com.example.c424.bean.ad.ReportNativeAd;
import com.example.c424.utils.ad.AdStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class ReportNativeAdManager {
    private volatile static ReportNativeAdManager Instance;
    private ReportNativeAdManager() {

    }
    public static ReportNativeAdManager getInstance() {
        if (Instance == null) {
            synchronized (ReportNativeAdManager.class) {
                if (Instance == null) {
                    Instance = new ReportNativeAdManager();
                }
            }
        }
        return Instance;
    }

    private ReportNativeAd reportNativeAd = new ReportNativeAd();
    private ShowFinishListener showFinishListener;

    public ReportNativeAd getReportNativeAd() {
        return reportNativeAd;
    }

    public void setShowFinishListener(ShowFinishListener showFinishListener) {
        this.showFinishListener = showFinishListener;
    }

    public interface ShowFinishListener {
        void onShowFinish();
    }

    public void loadReportNativeAd(Context context, String adId) {
        LogUtil.e("开始拉取report原生广告");
        AdStatusUtil.getInstance().setReportAdStatus(Constant.Loading);
        AdLoader adLoader = new AdLoader.Builder(context, adId)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        //成功
                        AdStatusUtil.getInstance().setReportAdStatus(Constant.Has_Cache);
                        LogUtil.e("report原生广告拉取成功  ID:" + adId);
                        reportNativeAd.setNativeAd(nativeAd);
                        reportNativeAd.setTimeStamp(System.currentTimeMillis());
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        SpUtil.setTodayNativeAdClickCount(context);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        AdStatusUtil.getInstance().setReportAdStatus(Constant.No_Cache);
                        LogUtil.e("report原生广告拉取失败  ID:" + adId + "   loadAdError:" + loadAdError);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void showReportNativeAd(Activity activity, FrameLayout container) {
        container.removeAllViews();
        //
        NativeAdView nativeAdView = new NativeAdView(activity);
        if (activity instanceof MainActivity) {
            nativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.layout_small_native_ad, null);
        } else if (activity instanceof ReportActivity || activity instanceof SpeedActivity || activity instanceof LocationActivity) {
            nativeAdView = (NativeAdView) LayoutInflater.from(activity).inflate(R.layout.layout_native_ad, null);
            nativeAdView.setMediaView(nativeAdView.findViewById(R.id.mediaView));
        }
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.logo));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.appName));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.appDesc));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.installBtn));
        //
        container.addView(nativeAdView);
        if (activity instanceof ReportActivity || activity instanceof SpeedActivity || activity instanceof LocationActivity) {
            nativeAdView.getMediaView().setMediaContent(reportNativeAd.getNativeAd().getMediaContent());
        }
        if (nativeAdView.getIconView() != null) {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(reportNativeAd.getNativeAd().getIcon().getDrawable());
        }
        ((TextView) nativeAdView.getHeadlineView()).setText(reportNativeAd.getNativeAd().getHeadline());
        ((TextView) nativeAdView.getBodyView()).setText(reportNativeAd.getNativeAd().getBody());
        nativeAdView.setNativeAd(reportNativeAd.getNativeAd());
        //
        LogUtil.e("展示report原生成功");
        //
        //初始化
        AdStatusUtil.getInstance().setReportAdStatus(Constant.No_Cache);
        reportNativeAd.setNativeAd(null);
        reportNativeAd.setTimeStamp(0);

        if (showFinishListener != null) {
            showFinishListener.onShowFinish();
        }
    }
}
