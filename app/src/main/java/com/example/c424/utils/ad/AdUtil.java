package com.example.c424.utils.ad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.adversisement.FinishIntAdManager;
import com.example.c424.adversisement.HomeNativeAdManager;
import com.example.c424.adversisement.ReportNativeAdManager;
import com.example.c424.adversisement.StartIntAdManager;
import com.example.c424.adversisement.StartOpenAdManager;
import com.example.c424.bean.AdInfo;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;


public class AdUtil {
    private volatile static AdUtil Instance;

    private AdUtil() {

    }

    public static AdUtil getInstance() {
        if (Instance == null) {
            synchronized (AdUtil.class) {
                if (Instance == null) {
                    Instance = new AdUtil();
                }
            }
        }
        return Instance;
    }

    private int closeAdAction;
    private Activity activity;
    private Intent intent;
    private Handler handler = new Handler();

    public void setCloseAdAction(int closeAdAction) {
        this.closeAdAction = closeAdAction;
    }

    public int getCloseAdAction() {
        return closeAdAction;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void initAdListener() {
        StartOpenAdManager.getInstance().setCloseAdListener(new StartOpenAdManager.CloseAdListener() {
            @Override
            public void onAdClose() {
                checkStartAdCache(App.app);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (activity != null) {
                            if (closeAdAction == Constant.CLOSE_AD_ACTION_FINISH) {
                                activity.finish();
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP) {
                                activity.startActivity(intent);
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP_FINISH) {
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        }
                    }
                }, 200);
            }
        });
        StartIntAdManager.getInstance().setCloseAdListener(new StartIntAdManager.CloseAdListener() {
            @Override
            public void onAdClose() {
                checkStartAdCache(App.app);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (activity != null) {
                            if (closeAdAction == Constant.CLOSE_AD_ACTION_FINISH) {
                                activity.finish();
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP) {
                                activity.startActivity(intent);
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP_FINISH) {
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        }
                    }
                }, 200);
            }
        });
        FinishIntAdManager.getInstance().setCloseAdListener(new FinishIntAdManager.CloseAdListener() {
            @Override
            public void onAdClose() {
                LogUtil.d("FinishIntAdManager close:" + closeAdAction);
                checkFinishAdCache(App.app);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (activity != null) {
                            if (closeAdAction == Constant.CLOSE_AD_ACTION_FINISH) {
                                activity.finish();
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP) {
                                activity.startActivity(intent);
                            } else if (closeAdAction == Constant.CLOSE_AD_ACTION_JUMP_FINISH) {
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        }
                    }
                }, 200);
            }
        });
        //
        HomeNativeAdManager.getInstance().setShowFinishListener(new HomeNativeAdManager.ShowFinishListener() {
            @Override
            public void onShowFinish() {
                checkHomeAdCache(App.app);
            }
        });
        ReportNativeAdManager.getInstance().setShowFinishListener(new ReportNativeAdManager.ShowFinishListener() {
            @Override
            public void onShowFinish() {
                checkReportAdCache(App.app);
            }
        });
    }

    /**
     * 检查所有广告为的缓存，没有缓存的话，就拉取
     */
    public void checkAllAdCache(Context context) {
        AdInfo startAdInfo = SpUtil.getAdInfoByLocation(context, Constant.START_AD_LOCATION);
        AdInfo finishAdInfo = SpUtil.getAdInfoByLocation(context, Constant.FINISH_AD_LOCATION);
        AdStatusUtil.getInstance().setShareFinishToStart((startAdInfo.isAdSwitch() && finishAdInfo.isAdSwitch()) || (!startAdInfo.isAdSwitch() && !finishAdInfo.isAdSwitch()));
        AdStatusUtil.getInstance().setShareStartToFinish(Constant.INT_AD_TYPE.equals(startAdInfo.getType()) && ((startAdInfo.isAdSwitch() && finishAdInfo.isAdSwitch()) || (!startAdInfo.isAdSwitch() && !finishAdInfo.isAdSwitch())));
        AdInfo homeAdInfo = SpUtil.getAdInfoByLocation(context, Constant.HOME_AD_LOCATION);
        AdInfo reportAdInfo = SpUtil.getAdInfoByLocation(context, Constant.REPORT_AD_LOCATION);
        AdStatusUtil.getInstance().setShareHomeReport(Constant.NAV_AD_TYPE.equals(homeAdInfo.getType()) && ((homeAdInfo.isAdSwitch() && reportAdInfo.isAdSwitch()) || (!homeAdInfo.isAdSwitch() && !reportAdInfo.isAdSwitch())));
        checkStartAdCache(context);
        checkHomeAdCache(context);
        checkFinishAdCache(context);
        checkReportAdCache(context);
    }

    private void checkStartAdCache(Context context) {
        AdStatusUtil.getInstance().setNeedLoadStartAd(false);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(context, Constant.START_AD_LOCATION);
        if (adInfo != null) {
            if (Constant.OPEN_AD_TYPE.equals(adInfo.getType())) {//广告类型为开屏
                if (StartOpenAdManager.getInstance().getStartOpenAd().getAppOpenAd() != null) {//有缓存
                    if (isAdTimeOut(StartOpenAdManager.getInstance().getStartOpenAd().getTimeStamp())) {//广告过期
                        if (adInfo.isAdSwitch()) {
                            AdStatusUtil.getInstance().setNeedLoadStartAd(true);
                            StartOpenAdManager.getInstance().loadStartOpenAd(context, adInfo.getAdId());
                        }
                    }
                } else if (AdStatusUtil.getInstance().getStartAdStatus() != Constant.Loading) {//没有缓存，不是正在拉取
                    if (adInfo.isAdSwitch()) {
                        AdStatusUtil.getInstance().setNeedLoadStartAd(true);
                        StartOpenAdManager.getInstance().loadStartOpenAd(context, adInfo.getAdId());
                    }
                }
            } else if (Constant.INT_AD_TYPE.equals(adInfo.getType())) {//广告类型为插页
                if (StartIntAdManager.getInstance().getStartIntAd().getInterstitialAd() != null) {//有缓存
                    if (isAdTimeOut(StartIntAdManager.getInstance().getStartIntAd().getTimeStamp())) {//广告过期
                        if (adInfo.isAdSwitch()) {
                            AdStatusUtil.getInstance().setNeedLoadStartAd(true);
                            StartIntAdManager.getInstance().loadStartIntAd(context, adInfo.getAdId());
                        }
                    }
                } else if (AdStatusUtil.getInstance().getStartAdStatus() != Constant.Loading) {//没有缓存，不是正在拉取
                    if (adInfo.isAdSwitch()) {
                        AdStatusUtil.getInstance().setNeedLoadStartAd(true);
                        StartIntAdManager.getInstance().loadStartIntAd(context, adInfo.getAdId());
                    }
                }
            }
        }
    }

    private void checkHomeAdCache(Context context) {
        AdStatusUtil.getInstance().setNeedLoadHomeAd(false);
        int maxNativeAdClick = SpUtil.getMaxNativeAdClickTime(context);
        int nativeAdClick = SpUtil.getTodayNativeAdClickCount(context);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(context, Constant.HOME_AD_LOCATION);
        if (adInfo != null) {
            if (Constant.NAV_AD_TYPE.equals(adInfo.getType())) {
                if (HomeNativeAdManager.getInstance().getHomeNativeAd().getNativeAd() != null) {
                    if (isAdTimeOut(HomeNativeAdManager.getInstance().getHomeNativeAd().getTimeStamp())) {
                        if (adInfo.isAdSwitch()) {
                            if (maxNativeAdClick > nativeAdClick) {
                                AdStatusUtil.getInstance().setNeedLoadHomeAd(true);
                                HomeNativeAdManager.getInstance().loadHomeNativeAd(context, adInfo.getAdId());
                            }
                        }
                    }
                } else if (AdStatusUtil.getInstance().getHomeAdStatus() != Constant.Loading) {
                    if (adInfo.isAdSwitch()) {
                        if (maxNativeAdClick > nativeAdClick) {
                            AdStatusUtil.getInstance().setNeedLoadHomeAd(true);
                            HomeNativeAdManager.getInstance().loadHomeNativeAd(context, adInfo.getAdId());
                        }
                    }
                }
            }
        }
    }

    private void checkFinishAdCache(Context context) {
        AdStatusUtil.getInstance().setNeedLoadFinishAd(false);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(context, Constant.FINISH_AD_LOCATION);
        if (adInfo != null) {
            if (FinishIntAdManager.getInstance().getFinishIntAd().getInterstitialAd() != null) {
                if (isAdTimeOut(FinishIntAdManager.getInstance().getFinishIntAd().getTimeStamp())) {
                    if (adInfo.isAdSwitch()) {
                        AdStatusUtil.getInstance().setNeedLoadFinishAd(true);
                        FinishIntAdManager.getInstance().loadFinishIntAd(context, adInfo.getAdId());
                    }
                }
            } else if (AdStatusUtil.getInstance().getFinishAdStatus() != Constant.Loading) {
                if (adInfo.isAdSwitch()) {
                    AdStatusUtil.getInstance().setNeedLoadFinishAd(true);
                    FinishIntAdManager.getInstance().loadFinishIntAd(context, adInfo.getAdId());
                }
            }
        }
    }

    private void checkReportAdCache(Context context) {
        int maxNativeAdClick = SpUtil.getMaxNativeAdClickTime(context);
        int nativeAdClick = SpUtil.getTodayNativeAdClickCount(context);
        AdStatusUtil.getInstance().setNeedLoadReportAd(false);
        AdInfo adInfo = SpUtil.getAdInfoByLocation(context, Constant.REPORT_AD_LOCATION);
        if (adInfo != null) {
            LogUtil.d("checkReportAdCache adInfo != null");
            LogUtil.d("checkReportAdCache AdStatusUtil.getInstance().getReportAdStatus() " + AdStatusUtil.getInstance().getReportAdStatus());
            if (ReportNativeAdManager.getInstance().getReportNativeAd().getNativeAd() != null) {
                if (isAdTimeOut(ReportNativeAdManager.getInstance().getReportNativeAd().getTimeStamp())) {
                    if (adInfo.isAdSwitch()) {
                        if (maxNativeAdClick > nativeAdClick) {
                            AdStatusUtil.getInstance().setNeedLoadReportAd(true);
                            ReportNativeAdManager.getInstance().loadReportNativeAd(context, adInfo.getAdId());
                        }
                    }
                }
            } else if (AdStatusUtil.getInstance().getReportAdStatus() != Constant.Loading) {
                LogUtil.d("checkReportAdCache report ad switch:" + adInfo.isAdSwitch());
                if (adInfo.isAdSwitch()) {
                    LogUtil.d("checkReportAdCache " + maxNativeAdClick + "   " + nativeAdClick);
                    if (maxNativeAdClick > nativeAdClick) {
                        AdStatusUtil.getInstance().setNeedLoadReportAd(true);
                        ReportNativeAdManager.getInstance().loadReportNativeAd(context, adInfo.getAdId());
                    }
                }
            }
        }
    }

    public boolean isAdTimeOut(long timestamp) {
        if (System.currentTimeMillis() - timestamp <= 60 * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }
}
