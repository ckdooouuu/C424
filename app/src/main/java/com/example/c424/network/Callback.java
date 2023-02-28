package com.example.c424.network;

import com.example.c424.bean.ResponseBean;

import io.reactivex.observers.DisposableObserver;

public class Callback extends DisposableObserver<ResponseBean> {
    OnCallbackListener onCallbackListener;

    public Callback(OnCallbackListener onCallbackListener) {
        this.onCallbackListener = onCallbackListener;
    }

    @Override
    public void onNext(ResponseBean responseBean) {
        onCallbackListener.onSuccess(responseBean);
    }

    @Override
    public void onError(Throwable e) {
        onCallbackListener.onError("Request error : " + e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public interface OnCallbackListener {
        void onError(String errorMsg);
        void onSuccess(ResponseBean responseBean);
    }
}
