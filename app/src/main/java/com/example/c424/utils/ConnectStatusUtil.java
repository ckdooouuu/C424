package com.example.c424.utils;

public class ConnectStatusUtil {
    private static volatile ConnectStatusUtil Instance;
    private ConnectStatusChangeListener connectStatusChangeListener;

    private ConnectStatusUtil() {

    }

    public void setConnectStatusChangeListener(ConnectStatusChangeListener connectStatusChangeListener) {
        this.connectStatusChangeListener = connectStatusChangeListener;
    }

    public static ConnectStatusUtil getInstance() {
        if (Instance == null) {
            synchronized (ConnectStatusUtil.class) {
                if (Instance == null) {
                    Instance = new ConnectStatusUtil();
                }
            }
        }
        return Instance;
    }

    private int status;

    public void setStatus(int status) {
        this.status = status;
        if (connectStatusChangeListener != null) {
            connectStatusChangeListener.connectStatusChange(status);
        }
    }

    public int getStatus() {
        return status;
    }

    public interface ConnectStatusChangeListener {
        void connectStatusChange(int s);
    }
}
