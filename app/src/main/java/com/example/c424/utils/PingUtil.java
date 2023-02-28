package com.example.c424.utils;

import android.content.Context;

import com.airbnb.lottie.L;
import com.example.c424.App;
import com.example.c424.bean.ProxyInfo;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PingUtil {
    private volatile static int index = 0;
    private PingFinishListener pingFinishListener;
    private CopyOnWriteArrayList<ProxyInfo> priorityCountryProxyList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<ProxyInfo> commonProxyList = new CopyOnWriteArrayList<>();

    private PingUtil() {

    }

    private static volatile PingUtil INSTANCE;

    public static PingUtil getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (PingUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PingUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void setPingFinishListener(PingFinishListener pingFinishListener) {
        this.pingFinishListener = pingFinishListener;
    }

    public interface PingFinishListener {
        void pingFinish();
    }

    public void pingProxy(Context context, List<ProxyInfo> proxyInfoList) {
        List<String> priorityCountryList = SpUtil.getPriorityCountry(App.app);
        index = 0;
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(30, 30, 30,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        commonProxyList.clear();
        priorityCountryProxyList.clear();
        for (int i = 0; i < proxyInfoList.size(); i++) {
            ProxyInfo proxyInfo = proxyInfoList.get(i);
            customPool.execute(new PingRunnable(context, proxyInfo, false, InetAddress.class, new PingRunnable.PingFinishListener() {//customPool.execute
                @Override
                public void onPingFinishListener(String ip, ProxyInfo proxy) {
                    synchronized (PingUtil.class) {
                        index++;
                        if (proxy.getDelay() < 1000) {
                            if (priorityCountryList.contains(proxy.getServerCouAbbr().toUpperCase())) {
                                priorityCountryProxyList.add(proxy);
                            } else {
                                commonProxyList.add(proxy);
                            }
                        }
                        if (index == proxyInfoList.size() - 1) {
                            if (priorityCountryProxyList.size() > 0) {
                                Collections.sort(priorityCountryProxyList, comparator);
                            }
                            if (commonProxyList.size() > 0) {
                                Collections.sort(commonProxyList, comparator);
                            }

                            App.app.proxyInfoList.clear();
                            if (priorityCountryProxyList.size() > 0) {
                                App.app.proxyInfoList.addAll(priorityCountryProxyList);
                            }
                            if (commonProxyList.size() > 0) {
                                App.app.proxyInfoList.addAll(commonProxyList);
                            }

                            App.app.proxyCountryList.clear();
                            for (int j = 0; j < App.app.proxyInfoList.size(); j++) {
                                if (App.app.proxyCountryList != null) {
                                    if (!App.app.proxyCountryList.contains(App.app.proxyInfoList.get(j).getServerCouAbbr().toUpperCase())) {
                                        App.app.proxyCountryList.add(App.app.proxyInfoList.get(j).getServerCouAbbr().toUpperCase());
                                    }
                                }
                            }
                            LogUtil.d("country size:" + App.app.proxyCountryList.size());
                            if (pingFinishListener != null) {
                                pingFinishListener.pingFinish();
                            }
                            LogUtil.d("ping finish");
                        }
                    }
                }

                @Override
                public void onPingErrorListener(String ip, ProxyInfo proxy) {
                }
            }));
        }
    }

    Comparator<ProxyInfo> comparator = (proxyInfo1, proxyInfo2) -> {
        long diff = proxyInfo1.getDelay() - proxyInfo2.getDelay();
        if (diff > 0) {
            return 1;
        } else {
            return -1;
        }
    };
}
