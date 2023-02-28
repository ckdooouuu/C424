package com.example.c424.network;


import io.reactivex.Observable;

import com.example.c424.bean.BaseReqBean;
import com.example.c424.bean.GlobalConfig;
import com.example.c424.bean.ProxyCollect;
import com.example.c424.bean.ReqFeedback;
import com.example.c424.bean.ReqUploadConnectionResult;
import com.example.c424.bean.ResponseBean;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("36AC9BA28A0B258837F32E0C0408F142")
    Observable<ResponseBean<GlobalConfig>> getGlobalConfig(@Body BaseReqBean baseReqBean);

    @POST("C0205017EDF1A110D26E089E28AC3C0E")
    Observable<ResponseBean<ResponseBean>> uploadConnectResult(@Body ReqUploadConnectionResult reqUploadConnectionResult);

    @POST("6992FEA638C34628FF25845C98939473")
    Observable<ResponseBean<ProxyCollect>> getProxyList(@Body BaseReqBean baseReqBean);

    @POST("9DAA69524A3B91A0A8E152CA510417D8")
    Observable<ResponseBean<ResponseBean>> feedback(@Body ReqFeedback reqFeedback);
}
