package com.example.c424.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.example.c424.bean.ProxyInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PingRunnable implements Runnable {
    Context context;
    final private StringBuilder mSb = new StringBuilder();
    private ProxyInfo proxyInfo;
    final private boolean mWifi;
    final private Class<? extends InetAddress> mInetClass;

    private Ping mPing;

    public PingFinishListener pingFinishListener;

    public PingRunnable(Context context, ProxyInfo proxyInfo, final boolean wifi, final Class<? extends InetAddress> inetClass, PingFinishListener pingFinishListener) {
        this.context = context;
        this.proxyInfo = proxyInfo;
        mWifi = wifi;
        mInetClass = inetClass;
        this.pingFinishListener = pingFinishListener;
    }

    public static Network getNetwork(final Context context, final int transport) {
        final ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (Network network : connManager.getAllNetworks()) {
            NetworkCapabilities networkCapabilities = connManager.getNetworkCapabilities(network);
            if (networkCapabilities != null &&
                    networkCapabilities.hasTransport(transport) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                return network;
            }
        }
        return null;
    }

    static InetAddress getInetAddress(final String host, Class<? extends InetAddress> inetClass) throws UnknownHostException {
        final InetAddress[] inetAddresses = InetAddress.getAllByName(host);
        InetAddress dest = null;
        for (final InetAddress inetAddress : inetAddresses) {
            if (inetClass.equals(inetAddress.getClass())) {
                return inetAddress;
            }
        }
        throw new UnknownHostException("Could not find IP address of type " + inetClass.getSimpleName());
    }

    @Override
    public void run() {
        try {
            InetAddress dest;
            if (mInetClass == InetAddress.class) {
                dest = InetAddress.getByName(proxyInfo.getHostServer());
            } else {
                dest = getInetAddress(proxyInfo.getHostServer(), mInetClass);
            }
//            MyLog.getInstance().d("host:" + proxyInfo.getKahoVpsIp() + "    " + dest.getHostAddress());
            InetAddress finalDest = dest;
            Ping ping = new Ping(proxyInfo, dest, new Ping.PingListener() {
                @Override
                public void onPing(final int count, ProxyInfo proxyInfo) {
                    pingFinishListener.onPingFinishListener(finalDest.getHostAddress(), proxyInfo);
                }

                @Override
                public void onPingException(final Exception e, final int count, ProxyInfo proxyInfo) {
                    pingFinishListener.onPingErrorListener(finalDest.getHostAddress(), proxyInfo);
                }

            });

            mPing = ping;
            ping.run();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LogUtil.d("PingRunnable Exception:" + e.getMessage());
        }
    }

    public void cancel() {
        if (mPing != null) {
            mPing.setCount(0);
        }
    }

    public interface PingFinishListener {
        void onPingFinishListener(String ip, ProxyInfo proxyInfo);

        void onPingErrorListener(String ip, ProxyInfo proxyInfo);
    }
}
