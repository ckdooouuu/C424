package com.example.c424.network;


import android.telecom.Call;

import io.reactivex.Observable;

import com.example.c424.bean.BaseReqBean;
import com.example.c424.bean.GlobalConfig;
import com.example.c424.bean.ProxyCollect;
import com.example.c424.bean.ReqFeedback;
import com.example.c424.bean.ReqUploadConnectionResult;
import com.example.c424.bean.ResponseBean;

public class RequestManager {
    public static void getGlobalConfig(BaseReqBean baseReqBean, Callback callback) {
        Observable<ResponseBean<GlobalConfig>> observable = RequestService.getInstance().getRequestInterface().getGlobalConfig(baseReqBean);
        RequestService.getInstance().toSubscribe(observable, callback);
    }

    public static void uploadConnectResult(ReqUploadConnectionResult reqUploadConnectionResult, Callback callback) {
        Observable<ResponseBean<ResponseBean>> observable = RequestService.getInstance().getRequestInterface().uploadConnectResult(reqUploadConnectionResult);
        RequestService.getInstance().toSubscribe(observable, callback);
    }

    public static void getProxyList(BaseReqBean baseReqBean, Callback callback) {
        Observable<ResponseBean<ProxyCollect>> observable = RequestService.getInstance().getRequestInterface().getProxyList(baseReqBean);
        RequestService.getInstance().toSubscribe(observable, callback);
    }

    public static void feedback(ReqFeedback reqFeedback, Callback callback) {
        Observable<ResponseBean<ResponseBean>> observable = RequestService.getInstance().getRequestInterface().feedback(reqFeedback);
        RequestService.getInstance().toSubscribe(observable, callback);
    }
}
