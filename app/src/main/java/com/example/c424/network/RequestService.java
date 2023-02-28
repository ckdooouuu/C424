package com.example.c424.network;

import com.example.c424.bean.ResponseBean;
import com.example.c424.utils.LogUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RequestService {
    private static final int CONNECT_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static String BASEURL = "http://ikz.umolewe.top";

    private Retrofit retrofit;
    private int RETRY_COUNT = 0;
    private OkHttpClient.Builder okHttpBuilder;
    private RequestInterface requestInterface;

    private static volatile RequestService INSTANCE = null;

    public static RequestService getInstance() {
        if (INSTANCE == null) {
            synchronized (RequestService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RequestService();
                }
            }
        }
        return INSTANCE;
    }

    private RequestService() {
        okHttpBuilder = new OkHttpClient.Builder();
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Accept-Encoding", "gzip")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        okHttpBuilder.addInterceptor(headerInterceptor);


//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                LogUtil.d(message);
//            }
//        });
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        //设置 Debug Log 模式
//        okHttpBuilder.addInterceptor(loggingInterceptor);


        okHttpBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        okHttpBuilder.retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(new ConverterManager(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();
        requestInterface = retrofit.create(RequestInterface.class);
    }

    public Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new MyTypeAdapterFactory())
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }

    public class MyTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawType = type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new MyTypeAdapter();
        }
    }

    public class MyTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }

    public RequestInterface getRequestInterface() {
        return requestInterface;
    }

    public <T> void toSubscribe(Observable<ResponseBean<T>> o, Callback s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(RETRY_COUNT)//请求失败重连次数
                .subscribe(s);
    }
}
