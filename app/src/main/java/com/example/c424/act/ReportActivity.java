package com.example.c424.act;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.utils.AssetsUtil;
import com.example.c424.utils.ConnectStatusUtil;
import com.example.c424.utils.SpUtil;
import com.example.c424.utils.ad.AdUtil;
import com.example.c424.utils.ad.ShowAdUtil;
import com.example.c424.view.ReportNativeLayout;
import com.example.c424.view.ReportRateLayout;
import com.google.android.gms.ads.nativead.NativeAdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends Activity {
    @BindView(R.id.reportQueueLayout)
    LinearLayout reportQueueLayout;
    @BindView(R.id.resultIcon)
    ImageView resultIcon;
    @BindView(R.id.resultFlag)
    ImageView resultFlag;
    @BindView(R.id.proxyName)
    TextView proxyName;

    ReportRateLayout reportRateLayout;
    ReportNativeLayout reportNativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ButterKnife.bind(this);

        App.app.isNeedUpdateNativeAd = true;
        reportRateLayout = new ReportRateLayout(this);
        reportNativeLayout = new ReportNativeLayout(this);
        initView();
    }

    private void initView() {
        reportQueueLayout.addView(reportNativeLayout);
        if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
            reportQueueLayout.addView(reportRateLayout);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) reportNativeLayout.getLayoutParams();
        lp.setMargins(0, 30, 0, 0);
        reportNativeLayout.setLayoutParams(lp);


        boolean isSuccess = getIntent().getBooleanExtra("isSuccess", false);
        String country = getIntent().getStringExtra("country");
        String alias = getIntent().getStringExtra("alias");
        String flagName = getIntent().getStringExtra("flag");

        if (isSuccess) {
            resultIcon.setImageResource(R.mipmap.pic_successful);
        } else {
            resultIcon.setImageResource(R.mipmap.pic_fail);
        }

        Bitmap flag2 = AssetsUtil.getFlag(ReportActivity.this, flagName.toUpperCase());
        if (flag2 != null) {
            Glide.with(ReportActivity.this)
                    .load(flag2)
                    .into(new SimpleTarget<Drawable>() {//这样处理，刷新列表的时候图片就不会闪烁
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                            resultFlag.setImageDrawable(resource);
                        }
                    });
        }

        proxyName.setText(country + " " + alias);
    }

    private void removeNativeAd() {
        if (reportNativeLayout.getNativeAdContainer() != null) {
            if (reportNativeLayout.getNativeAdContainer().getChildCount() > 0) {
                NativeAdView nativeAdView = (NativeAdView) reportNativeLayout.getNativeAdContainer().getChildAt(0);
                nativeAdView.destroy();
                reportNativeLayout.getNativeAdContainer().removeAllViews();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.app.isNeedUpdateNativeAd) {
            App.app.isNeedUpdateNativeAd = false;
            removeNativeAd();
            ShowAdUtil.showReportAd(ReportActivity.this, reportNativeLayout);
        }
    }

    @OnClick(R.id.backBtn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                showExtraAd();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showExtraAd();
    }

    private void showExtraAd() {
        App.app.isNeedUpdateNativeAd = true;
        if (SpUtil.getBackFromReportAdSwitch(this)) {
            AdUtil.getInstance().setActivity(ReportActivity.this);
            AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_FINISH);
            AdUtil.getInstance().setIntent(null);
            ShowAdUtil.showFinishAd(ReportActivity.this);
        } else {
            finish();
        }
    }
}
