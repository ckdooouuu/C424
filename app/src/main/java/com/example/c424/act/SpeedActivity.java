package com.example.c424.act;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.utils.ConnectStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpeedUtil;
import com.example.c424.utils.ad.ShowAdUtil;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeedActivity extends Activity {
    @BindView(R.id.unConnectTipLayout)
    LinearLayout unConnectTipLayout;
    @BindView(R.id.showSpeedLayout)
    LinearLayout showSpeedLayout;
    @BindView(R.id.uploadSpeedTv)
    TextView uploadSpeedTv;
    @BindView(R.id.downloadSpeedTv)
    TextView downloadSpeedTv;
    @BindView(R.id.nativeAdLayout)
    FrameLayout nativeAdLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        ButterKnife.bind(this);

        initView();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String upSpeed = SpeedUtil.getTotalUpSpeed(SpeedActivity.this);
                String downSpeed = SpeedUtil.getTotalDownloadSpeed(SpeedActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadSpeedTv.setText(setSpeedText(upSpeed));
                        downloadSpeedTv.setText(setSpeedText(downSpeed));
                    }
                });
            }
        };
        if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
            timer.schedule(timerTask, 1000, 5000);
        }
    }

    private void initView() {
        if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
            showSpeedLayout.setVisibility(View.VISIBLE);
            unConnectTipLayout.setVisibility(View.GONE);
        } else {
            showSpeedLayout.setVisibility(View.GONE);
            unConnectTipLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.backBtn, R.id.connectBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                App.app.isNeedUpdateNativeAd = true;
                finish();
                break;
            case R.id.connectBtn:
                setResult(6001);
                finish();
                break;
        }
    }

    private void removeNativeAd() {
        if (nativeAdLayout != null) {
            if (nativeAdLayout.getChildCount() > 0) {
                NativeAdView nativeAdView = (NativeAdView) nativeAdLayout.getChildAt(0);
                nativeAdView.destroy();
                nativeAdLayout.removeAllViews();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.app.isNeedUpdateNativeAd) {
            removeNativeAd();
            App.app.isNeedUpdateNativeAd = false;
            ShowAdUtil.showReportAd(SpeedActivity.this, nativeAdLayout);
        }
    }

    @Override
    public void onBackPressed() {
        App.app.isNeedUpdateNativeAd = true;
        super.onBackPressed();
    }

    private SpannableString setSpeedText(String speedStr) {
        //获取首字母下标
        LogUtil.d("speedStr:" + speedStr);
        Pattern p = Pattern.compile("\\p{L}");
        Matcher m = p.matcher(speedStr);
        int index = 0;
        if (m.find()) {
            index = m.start();
        }
        //
        SpannableString spannableString = new SpannableString(speedStr);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.7f);
        spannableString.setSpan(relativeSizeSpan, index, speedStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
