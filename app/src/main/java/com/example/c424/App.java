package com.example.c424;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.example.c424.act.SplashActivity;
import com.example.c424.bean.BaseReqBean;
import com.example.c424.bean.GlobalConfig;
import com.example.c424.bean.ProxyCollect;
import com.example.c424.bean.ProxyInfo;
import com.example.c424.bean.ResponseBean;
import com.example.c424.network.Callback;
import com.example.c424.network.RequestManager;
import com.example.c424.utils.ad.AdUtil;
import com.example.c424.utils.GoogleIdUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.PingUtil;
import com.example.c424.utils.SpUtil;
import com.example.c424.view.MyRefreshHeader;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.blinkt.openvpn.core.ICSOpenVPNApplication;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App extends ICSOpenVPNApplication {
    public static App app;
    public int SDK_VERSION = 120;
    public String googleID;
    public String countryName;
    public long firstInstallTime;
    public String simCountry;
    public List<ProxyInfo> proxyInfoList = new ArrayList<>();
    public List<String> proxyCountryList = new ArrayList<>();
    public boolean isNeedUpdateNativeAd = false;
    public boolean isNeedShowUpgradeDialog = false;
    public boolean hasIntoMain = false;//是否已经进入过首页

    private int activityCount = 0;
    public boolean isBackground = false;
    public String lastActivityName = "";

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout refreshLayout) {
                return new MyRefreshHeader(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LogUtil.d("getPackageName:" + getPackageName() + "    " + getProcessName());
            if (getProcessName().equals(getPackageName())) {
                initApp();
            } else {
                WebView.setDataDirectorySuffix("ads_dir");
            }
        } else {
            String processName = getProcessName2(this, android.os.Process.myPid());
            LogUtil.d("processName:" + processName + "    " + getPackageName());
            if (processName.equals(getPackageName())) {
                initApp();
            }
        }
    }

    public static String getProcessName2(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    private void initApp() {
        LogUtil.d("0000000000000000000000");
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        if (!BuildConfig.DEBUG) {
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);
        } else {
            firebaseCrashlytics.setCrashlyticsCollectionEnabled(true);
        }

        FacebookSdk.setApplicationId("624014406226892");//TODO
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        FacebookSdk.setClientToken("e2884b0d12303a324a0e8833ad09e4dd");//TODO
        FacebookSdk.setAutoLogAppEventsEnabled(true);

        registerLifecycle();
        getLocation();
        getGlobalConfig();
        getProxyList();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdUtil.getInstance().initAdListener();
        if (!SpUtil.isFirstLaunch(app)) {
            AdUtil.getInstance().checkAllAdCache(app);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GoogleIdUtil.getGoogleAdId(App.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("getGoogleAdId Exception:" + e.getMessage());
                }
            }
        }).start();
    }

    private void getLocation() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://ipwho.is/").method("GET", null).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("getLocation error:" + e.getMessage());

                getLocationBySim(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String locationStr = response.body().string();
                    LogUtil.d("getLocation str:" + locationStr);
                    JSONObject jsonObject = new JSONObject(locationStr);
                    countryName = jsonObject.getString("country_code");
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("MyApp getLocation onResponse Exception:" + e.getMessage());

                    getLocationBySim(true);
                }
            }
        });
    }

    private String getLocationBySim(boolean isCountryNull) {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String country = telManager.getSimCountryIso();
        if (isCountryNull) {
            countryName = country;
        }
        simCountry = country;
        return country;
    }

    private long getFirstInstallTime() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            firstInstallTime = info.firstInstallTime;
            return firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getFirstInstallTime Exception:" + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    private void getGlobalConfig() {
        String androidID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        BaseReqBean baseReqBean = new BaseReqBean(getPackageName(), SDK_VERSION, androidID, googleID,
                Build.VERSION.SDK_INT, "en", countryName, System.currentTimeMillis(), "",
                getLocationBySim((countryName == null) || (countryName.isEmpty())), getFirstInstallTime());
        LogUtil.d("getGlobalConfig BaseReqBean:" + baseReqBean.toString());
        RequestManager.getGlobalConfig(baseReqBean, new Callback(new Callback.OnCallbackListener() {
            @Override
            public void onError(String errorMsg) {
                LogUtil.d("getGlobalConfig errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                LogUtil.e("getGlobalConfig Response:" + responseBean.toString());
                GlobalConfig globalConfig = (GlobalConfig) responseBean.getRespData();
                SpUtil.saveConfig(App.this, globalConfig);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGlobalConfig();
                    }
                }, globalConfig.getConfig_rocketspeedup_Interval() * 60 * 1000);
            }
        }));
    }

    private void getProxyList() {
        String androidID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        BaseReqBean baseReqBean = new BaseReqBean(getPackageName(), SDK_VERSION, androidID, googleID,
                Build.VERSION.SDK_INT, "en", countryName, System.currentTimeMillis(), "",
                getLocationBySim((countryName == null) || (countryName.isEmpty())), getFirstInstallTime());
        RequestManager.getProxyList(baseReqBean, new Callback(new Callback.OnCallbackListener() {
            @Override
            public void onError(String errorMsg) {
                LogUtil.d("getProxyList errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                ProxyCollect proxyCollect = (ProxyCollect) responseBean.getRespData();
//                LogUtil.d("getProxyList Response1:" + proxyCollect.getVsArrays());
//                LogUtil.d("getProxyList Response2:" + proxyCollect.getServerArrayPro());
                PingUtil.getINSTANCE().pingProxy(App.this, proxyCollect.getVsArrays());
            }
        }));
    }

    private void registerLifecycle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    activityCount++;
                    isBackground = false;
                    LogUtil.d("onActivityStarted activity:" + activity.getComponentName().getClassName() + "    " + activityCount);

                    if (activityCount == 1) {
                        if (!activity.getComponentName().getClassName().contains("SplashActivity")) {
                            isNeedUpdateNativeAd = false;
                            LogUtil.d("热启动 打开启动页");
                            Intent intent = new Intent(activity, SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            if (lastActivityName != null) {
                                if (lastActivityName.contains("AdActivity")) {
                                    LogUtil.d("热启动 关闭了广告页，打开启动页");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    LogUtil.d("onActivityStopped activity:" + activity.getComponentName().getClassName());
                    activityCount--;
                    if (activityCount <= 0) {
                        isBackground = true;

                        LogUtil.d("后台");
                        //                    isBackground = true;
                        lastActivityName = activity.getComponentName().getClassName();
                        if (activity instanceof AdActivity) {
                            LogUtil.d("关闭广告页");
                            activity.finish();
                        } else if (activity instanceof SplashActivity) {
                            activity.finish();
                        }
                    }
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                }
            });
        }
    }
}
