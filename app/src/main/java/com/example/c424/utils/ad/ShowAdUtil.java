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

public class ShowAdUtil {
    public static void showStartAd(Activity activity) {
        if (!App.app.isBackground) {
            AdUtil.getInstance().checkAllAdCache(activity);
            AdInfo adInfo = SpUtil.getAdInfoByLocation(activity, Constant.START_AD_LOCATION);
            if (Constant.OPEN_AD_TYPE.equals(adInfo.getType())) {
                StartOpenAdManager.getInstance().showStartOpenAd(activity);
            } else if (Constant.INT_AD_TYPE.equals(adInfo.getType())) {
                StartIntAdManager.getInstance().showStartIntAd(activity);
            }
        }
    }

    public static void showHomeAd(Activity activity, FrameLayout nativeAdContainer, FrameLayout bannerAdLayout) {
        AdUtil.getInstance().checkAllAdCache(activity);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(activity, Constant.HOME_AD_LOCATION);
        if (Constant.NAV_AD_TYPE.equals(adInfo.getType())) {
            if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {
                HomeNativeAdManager.getInstance().showHomeNativeAd(activity, nativeAdContainer);
            } else {
                if (AdStatusUtil.getInstance().isShareHomeReport()) {
                    if (AdStatusUtil.getInstance().getReportAdStatus() == Constant.Has_Cache) {
                        ReportNativeAdManager.getInstance().showReportNativeAd(activity, nativeAdContainer);
                    }
                }
            }
        } else if (Constant.BAN_AD_TYPE.equals(adInfo.getType())) {
            if (adInfo.isAdSwitch()) {
                HomeBannerAdManager.getInstance().showHomeBannerAd(activity, bannerAdLayout);
            }
        }
    }

    public static void showFinishAd(Activity activity) {
        if (!App.app.isBackground) {
            AdUtil.getInstance().checkAllAdCache(activity);
            if (AdStatusUtil.getInstance().getFinishAdStatus() == Constant.Has_Cache) {
                FinishIntAdManager.getInstance().showFinishIntAd(activity);
            } else {
                if (AdStatusUtil.getInstance().isShareStartToFinish()) {
                    if (AdStatusUtil.getInstance().getStartAdStatus() == Constant.Has_Cache) {
                        StartIntAdManager.getInstance().showStartIntAd(activity);
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
            ReportNativeAdManager.getInstance().showReportNativeAd(activity, container);
        } else {
            if (AdStatusUtil.getInstance().isShareHomeReport()) {
                if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {
                    HomeNativeAdManager.getInstance().showHomeNativeAd(activity, container);
                }
            }
        }
    }
}
