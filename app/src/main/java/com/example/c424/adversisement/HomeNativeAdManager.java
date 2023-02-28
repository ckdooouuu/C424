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
import com.example.c424.bean.ad.HomeNativeAd;
import com.example.c424.utils.ad.AdStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class HomeNativeAdManager {
    private volatile static HomeNativeAdManager Instance;
    private HomeNativeAdManager() {

    }

    public static HomeNativeAdManager getInstance() {
        if (Instance == null) {
            synchronized (HomeNativeAdManager.class) {
                if (Instance == null) {
                    Instance = new HomeNativeAdManager();
                }
            }
        }
        return Instance;
    }

    private HomeNativeAd homeNativeAd = new HomeNativeAd();
    private LoadFinishListener loadFinishListener;
    private ShowFinishListener showFinishListener;

    public HomeNativeAd getHomeNativeAd() {
        return homeNativeAd;
    }

    public void setLoadFinishListener(LoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    public void setShowFinishListener(ShowFinishListener showFinishListener) {
        this.showFinishListener = showFinishListener;
    }

    public interface LoadFinishListener {
        void onLoadFinish(boolean isLoadSucceed);
    }

    public interface ShowFinishListener {
        void onShowFinish();
    }

    public void loadHomeNativeAd(Context context, String adId) {
        LogUtil.e("开始拉取home原生广告");
        AdStatusUtil.getInstance().setHomeAdStatus(Constant.Loading);
        AdLoader adLoader = new AdLoader.Builder(context, adId)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        //成功
                        LogUtil.e("home原生广告拉取成功  ID:" + adId);
                        AdStatusUtil.getInstance().setHomeAdStatus(Constant.Has_Cache);
                        homeNativeAd.setNativeAd(nativeAd);
                        homeNativeAd.setTimeStamp(System.currentTimeMillis());
                        if (loadFinishListener != null) {
                            loadFinishListener.onLoadFinish(true);
                        }
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
                        LogUtil.e("home原生广告拉取失败  ID:" + adId + "   loadAdError:" + loadAdError);
                        AdStatusUtil.getInstance().setHomeAdStatus(Constant.No_Cache);
                        if (loadFinishListener != null) {
                            loadFinishListener.onLoadFinish(false);
                        }
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void showHomeNativeAd(Activity activity, FrameLayout container) {
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
            nativeAdView.getMediaView().setMediaContent(homeNativeAd.getNativeAd().getMediaContent());
        }
        if (nativeAdView.getIconView() != null) {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(homeNativeAd.getNativeAd().getIcon().getDrawable());
        }
        ((TextView) nativeAdView.getHeadlineView()).setText(homeNativeAd.getNativeAd().getHeadline());
        ((TextView) nativeAdView.getBodyView()).setText(homeNativeAd.getNativeAd().getBody());
        nativeAdView.setNativeAd(homeNativeAd.getNativeAd());
        //
        LogUtil.e("展示home原生成功");
        //
        //初始化
        AdStatusUtil.getInstance().setHomeAdStatus(Constant.No_Cache);
        homeNativeAd.setNativeAd(null);
        homeNativeAd.setTimeStamp(0);
        //重新拉取
        if (showFinishListener != null) {
            showFinishListener.onShowFinish();
        }
    }

}
