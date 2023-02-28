package com.example.c424.adversisement;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.c424.Constant;
import com.example.c424.bean.AdInfo;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class HomeBannerAdManager {
    private volatile static HomeBannerAdManager Instance;

    private HomeBannerAdManager() {

    }

    public static HomeBannerAdManager getInstance() {
        if (Instance == null) {
            synchronized (HomeBannerAdManager.class) {
                if (Instance == null) {
                    Instance = new HomeBannerAdManager();
                }
            }
        }
        return Instance;
    }

    public void showHomeBannerAd(Activity activity, FrameLayout container) {
        AdInfo adInfo = SpUtil.getAdInfoByLocation(activity, Constant.HOME_AD_LOCATION);
        String adId = adInfo.getAdId();
        AdView adView = new AdView(activity);
        adView.setAdUnitId(adId);
        adView.setAdSize(getAdSize(activity, container));
        adView.loadAd(new AdRequest.Builder().build());
        LogUtil.d("展示banner ID:" + adId);
        String finalAdId = adId;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                LogUtil.d("home banner广告拉取失败 ID:" + finalAdId + "    " + loadAdError.toString());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                container.removeAllViews();
                //
                LogUtil.d("home banner广告拉取成功 ID:" + finalAdId);
                container.addView(adView);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                LogUtil.d("home banner广告展示成功 ID:" + finalAdId);
            }
        });
    }

    private AdSize getAdSize(Activity activity, FrameLayout container) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = container.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
}
