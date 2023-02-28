package com.example.c424.adversisement;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.c424.Constant;
import com.example.c424.bean.ad.StartIntAd;
import com.example.c424.utils.ad.AdStatusUtil;
import com.example.c424.utils.LogUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class StartIntAdManager {
    private static volatile StartIntAdManager Instance;
    private StartIntAdManager() {

    }

    public static StartIntAdManager getInstance() {
        if (Instance == null) {
            synchronized (StartIntAdManager.class) {
                if (Instance == null) {
                    Instance = new StartIntAdManager();
                }
            }
        }
        return Instance;
    }

    private StartIntAd startIntAd = new StartIntAd();
    private CloseAdListener closeAdListener;

    public StartIntAd getStartIntAd() {
        return startIntAd;
    }

    public void setCloseAdListener(CloseAdListener closeAdListener) {
        this.closeAdListener = closeAdListener;
    }

    public interface CloseAdListener {
        void onAdClose();
    }

    public void loadStartIntAd(Context context, String adId) {
        try {
            LogUtil.e("开始拉取start插页广告");
            AdStatusUtil.getInstance().setStartAdStatus(Constant.Loading);
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, adId, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                            LogUtil.e("拉取start插页广告失败 ID:" + adId);
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            AdStatusUtil.getInstance().setStartAdStatus(Constant.Has_Cache);
                            LogUtil.e("拉取start插页广告成功 ID:" + adId);
                            startIntAd.setTimeStamp(System.currentTimeMillis());
                            startIntAd.setInterstitialAd(interstitialAd);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("loadStartInterAd Exception:" + e.getMessage());
        }
    }

    public void showStartIntAd(Activity activity) {
        startIntAd.getInterstitialAd().show(activity);
        startIntAd.getInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                LogUtil.e("关闭start插页广告");
                if (closeAdListener != null) {
                    closeAdListener.onAdClose();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                LogUtil.e("start插页广告展示失败");
                AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                startIntAd.setTimeStamp(0);
                startIntAd.setInterstitialAd(null);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                LogUtil.e("start插页广告展示成功");
                AdStatusUtil.getInstance().setStartAdStatus(Constant.No_Cache);
                startIntAd.setTimeStamp(0);
                startIntAd.setInterstitialAd(null);
            }
        });
    }
}
