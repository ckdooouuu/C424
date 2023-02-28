package com.example.c424.utils.ad;

import android.app.Activity;
import android.widget.FrameLayout;

import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.adversisement.FinishIntAdManager;
import com.example.c424.adversisement.HomeBannerAdManager;
import com.example.c424.adversisement.HomeNativeAdManager;
import com.example.c424.adversisement.ReportNativeAdManager;
import com.example.c424.adversisement.StartIntAdManager;
import com.example.c424.adversisement.StartOpenAdManager;
import com.example.c424.bean.AdInfo;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAdView;

public class ShowAdUtil {
    public static void showStartAd(Activity activity) {
        if (!App.app.isBackground) {
            AdUtil.getInstance().checkAllAdCache(activity);
            AdInfo adInfo = SpUtil.getAdInfoByLocation(activity, Constant.START_AD_LOCATION);
            if (Constant.OPEN_AD_TYPE.equals(adInfo.getType())) {
                if (!AdUtil.getInstance().isAdTimeOut(StartOpenAdManager.getInstance().getStartOpenAd().getTimeStamp())) {
                    StartOpenAdManager.getInstance().showStartOpenAd(activity);
                }
            } else if (Constant.INT_AD_TYPE.equals(adInfo.getType())) {
                if (!AdUtil.getInstance().isAdTimeOut(StartIntAdManager.getInstance().getStartIntAd().getTimeStamp())) {
                    StartIntAdManager.getInstance().showStartIntAd(activity);
                }
            }
        }
    }

    public static void showHomeAd(Activity activity, FrameLayout nativeAdContainer, FrameLayout bannerAdLayout) {
        if (nativeAdContainer != null) {
            if (nativeAdContainer.getChildCount() > 0) {
                NativeAdView nativeAdView = (NativeAdView) nativeAdContainer.getChildAt(0);
                nativeAdView.destroy();
                nativeAdContainer.removeAllViews();
            }
        }

        AdUtil.getInstance().checkAllAdCache(activity);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(activity, Constant.HOME_AD_LOCATION);
        if (Constant.NAV_AD_TYPE.equals(adInfo.getType())) {

            if (bannerAdLayout != null) {
                if (bannerAdLayout.getChildCount() > 0) {
                    AdView adView = (AdView) bannerAdLayout.getChildAt(0);
                    adView.destroy();
                    bannerAdLayout.removeAllViews();
                }
            }

            if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {
                if (!AdUtil.getInstance().isAdTimeOut(HomeNativeAdManager.getInstance().getHomeNativeAd().getTimeStamp())) {
                    HomeNativeAdManager.getInstance().showHomeNativeAd(activity, nativeAdContainer);
                }
            } else {
                if (AdStatusUtil.getInstance().isShareHomeReport()) {
                    if (AdStatusUtil.getInstance().getReportAdStatus() == Constant.Has_Cache) {
                        if (!AdUtil.getInstance().isAdTimeOut(ReportNativeAdManager.getInstance().getReportNativeAd().getTimeStamp())) {
                            ReportNativeAdManager.getInstance().showReportNativeAd(activity, nativeAdContainer);
                        }
                    }
                }
            }
        } else if (Constant.BAN_AD_TYPE.equals(adInfo.getType())) {
            if (adInfo.isAdSwitch()) {
                if (bannerAdLayout.getChildCount() <= 0) {
                    HomeBannerAdManager.getInstance().showHomeBannerAd(activity, bannerAdLayout);
                }
            } else {
                if (bannerAdLayout != null) {
                    if (bannerAdLayout.getChildCount() > 0) {
                        AdView adView = (AdView) bannerAdLayout.getChildAt(0);
                        adView.destroy();
                        bannerAdLayout.removeAllViews();
                    }
                }
            }
        }
    }

    public static void showFinishAd(Activity activity) {
        if (!App.app.isBackground) {
            AdUtil.getInstance().checkAllAdCache(activity);
            if (AdStatusUtil.getInstance().getFinishAdStatus() == Constant.Has_Cache) {
                if (!AdUtil.getInstance().isAdTimeOut(FinishIntAdManager.getInstance().getFinishIntAd().getTimeStamp())) {
                    FinishIntAdManager.getInstance().showFinishIntAd(activity);
                }
            } else {
                if (AdStatusUtil.getInstance().isShareStartToFinish()) {
                    if (AdStatusUtil.getInstance().getStartAdStatus() == Constant.Has_Cache) {
                        if (!AdUtil.getInstance().isAdTimeOut(StartIntAdManager.getInstance().getStartIntAd().getTimeStamp())) {
                            StartIntAdManager.getInstance().showStartIntAd(activity);
                        }
                    } else {
                        FinishIntAdManager.getInstance().getCloseAdListener().onAdClose();
                    }
                } else {
                    FinishIntAdManager.getInstance().getCloseAdListener().onAdClose();
                }
            }
        }
    }

    public static void showReportAd(Activity activity, FrameLayout container) {
        AdUtil.getInstance().checkAllAdCache(activity);
        LogUtil.d("report ad status:" + AdStatusUtil.getInstance().getReportAdStatus());

        if (AdStatusUtil.getInstance().getReportAdStatus() == Constant.Has_Cache) {
            if (!AdUtil.getInstance().isAdTimeOut(ReportNativeAdManager.getInstance().getReportNativeAd().getTimeStamp())) {
                ReportNativeAdManager.getInstance().showReportNativeAd(activity, container);
            }
        } else {
            if (AdStatusUtil.getInstance().isShareHomeReport()) {
                if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {
                    if (!AdUtil.getInstance().isAdTimeOut(HomeNativeAdManager.getInstance().getHomeNativeAd().getTimeStamp())) {
                        HomeNativeAdManager.getInstance().showHomeNativeAd(activity, container);
                    }
                }
            }
        }
    }
}