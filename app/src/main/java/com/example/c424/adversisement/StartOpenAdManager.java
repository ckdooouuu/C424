package com.example.c424.adversisement;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.c424.Constant;
import com.example.c424.bean.ad.StartOpenAd;
import com.example.c424.utils.ad.AdStatusUtil;
import com.example.c424.utils.LogUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class StartOpenAdManager {
    private volatile static StartOpenAdManager Instance;

    private StartOpenAdManager() {

    }

    public static StartOpenAdManager getInstance() {
        if (Instance == null) {
            synchronized (StartOpenAdManager.class) {
                if (Instance == null) {
                    Instance = new StartOpenAdManager();
                }
            }
        }
        return Instance;
    }

    private StartOpenAd startOpenAd = new StartOpenAd();
    private CloseAdListener closeAdListener;

    public StartOpenAd getStartOpenAd() {
        return startOpenAd;
    }

    public void setCloseAdListener(CloseAdListener closeAdListener) {
        this.closeAdListener = closeAdListener;
    }

    public interface CloseAdListener {
        void onAdClose();
    }

    public void loadStartOpenAd(Context context, String adId) {
        try {
            LogUtil.e("开始拉取start开屏广告");
            AdStatusUtil.getInstance().setStartAdStatus(Constant.Loading);
            AdRequest adRequest = new AdRequest.Builder().build();
            AppOpenAd.load(context, adId, adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                            LogUtil.e("拉取start开屏广告失败 ID:" + adId + "    " + loadAdError.toString());
                        }

                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                            super.onAdLoaded(appOpenAd);
                            AdStatusUtil.getInstance().setStartAdStatus(Constant.Has_Cache);
                            LogUtil.e("拉取start开屏广告成功 ID:" + adId);
                            startOpenAd.setAppOpenAd(appOpenAd);
                            startOpenAd.setTimeStamp(System.currentTimeMillis());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("loadStartOpenAd Exception:" + e.getMessage());
        }
    }

    public void showStartOpenAd(Activity activity) {
        startOpenAd.getAppOpenAd().show(activity);
        startOpenAd.getAppOpenAd().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                LogUtil.e("关闭start开屏广告");
                if (closeAdListener != null) {
                    closeAdListener.onAdClose();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                LogUtil.e("start开屏广告展示失败   " + adError.toString());
                AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                startOpenAd.setTimeStamp(0);
                startOpenAd.setAppOpenAd(null);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                LogUtil.e("start开屏广告展示成功");
                AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                startOpenAd.setTimeStamp(0);
                startOpenAd.setAppOpenAd(null);
            }
        });
    }

}
