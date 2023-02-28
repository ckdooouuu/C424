package com.example.c424.adversisement;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.c424.Constant;
import com.example.c424.bean.ad.FinishIntAd;
import com.example.c424.utils.ad.AdStatusUtil;
import com.example.c424.utils.LogUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class FinishIntAdManager {
    private volatile static FinishIntAdManager Instance;
    private FinishIntAdManager() {

    }

    public static FinishIntAdManager getInstance() {
        if (Instance == null) {
            synchronized (FinishIntAdManager.class) {
                if (Instance == null) {
                    Instance = new FinishIntAdManager();
                }
            }
        }
        return Instance;
    }

    private FinishIntAd finishIntAd = new FinishIntAd();
    private CloseAdListener closeAdListener;

    public FinishIntAd getFinishIntAd() {
        return finishIntAd;
    }

    public void setCloseAdListener(CloseAdListener closeAdListener) {
        this.closeAdListener = closeAdListener;
    }

    public CloseAdListener getCloseAdListener() {
        return closeAdListener;
    }

    public interface CloseAdListener {
        void onAdClose();
    }

    public void loadFinishIntAd(Context context, String adId) {
        try {
            LogUtil.e("开始拉取finish插页广告");
            AdStatusUtil.getInstance().setFinishAdStatus(Constant.Loading);
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, adId, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            LogUtil.e("拉取finish插页广告失败 ID:" + adId);
                            AdStatusUtil.getInstance().setFinishAdStatus(Constant.No_Cache);
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            LogUtil.e("拉取finish插页广告成功 ID:" + adId);
                            AdStatusUtil.getInstance().setFinishAdStatus(Constant.Has_Cache);
                            finishIntAd.setTimeStamp(System.currentTimeMillis());
                            finishIntAd.setInterstitialAd(interstitialAd);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("loadFinishInterAd Exception:" + e.getMessage());
        }
    }

    public void showFinishIntAd(Activity activity) {
        finishIntAd.getInterstitialAd().show(activity);
        finishIntAd.getInterstitialAd().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                LogUtil.e("关闭Finish插页广告");
                if (closeAdListener != null) {
                    closeAdListener.onAdClose();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                LogUtil.e("Finish插页广告展示失败");
                AdStatusUtil.getInstance().setFinishAdStatus(Constant.No_Cache);
                finishIntAd.setTimeStamp(0);
                finishIntAd.setInterstitialAd(null);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                LogUtil.e("Finish插页广告展示成功");
                AdStatusUtil.getInstance().setFinishAdStatus(Constant.No_Cache);
                finishIntAd.setTimeStamp(0);
                finishIntAd.setInterstitialAd(null);
            }
        });
    }
}
