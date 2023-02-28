package com.example.c424.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.utils.ConnectStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.ad.ShowAdUtil;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationActivity extends Activity {
    @BindView(R.id.latitudeTv)
    TextView latitudeTv;
    @BindView(R.id.longitudeTv)
    TextView longitudeTv;
    @BindView(R.id.cityTv)
    TextView cityTv;
    @BindView(R.id.regionTv)
    TextView regionTv;
    @BindView(R.id.countryTv)
    TextView countryTv;
    @BindView(R.id.connectLayout)
    RelativeLayout connectLayout;
    @BindView(R.id.connectBtn)
    LinearLayout connectBtn;
    @BindView(R.id.nativeAdLayout)
    FrameLayout nativeAdLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ButterKnife.bind(this);

        if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
            connectLayout.setVisibility(View.GONE);
        }
        getLocationInfo();
    }

    @OnClick({R.id.backBtn, R.id.connectBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                App.app.isNeedUpdateNativeAd = true;
                finish();
                break;
            case R.id.connectBtn:
                setResult(7001);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.app.isNeedUpdateNativeAd) {
            App.app.isNeedUpdateNativeAd = false;
            ShowAdUtil.showReportAd(LocationActivity.this, nativeAdLayout);
        }
    }

    private void getLocationInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://ipwho.is/").method("GET", null).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("getLocation error:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String locationStr = response.body().string();
                            LogUtil.d("getLocation str:" + locationStr);
                            JSONObject jsonObject = new JSONObject(locationStr);
                            latitudeTv.setText(jsonObject.getString("latitude"));
                            longitudeTv.setText(jsonObject.getString("longitude"));
                            cityTv.setText(jsonObject.getString("city"));
                            regionTv.setText(jsonObject.getString("region"));
                            countryTv.setText(jsonObject.getString("country"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.d("MyApp getLocation onResponse Exception:" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        App.app.isNeedUpdateNativeAd = true;
        super.onBackPressed();
    }
}
