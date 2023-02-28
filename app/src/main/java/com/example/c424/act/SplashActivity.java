package com.example.c424.act;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.SpUtil;
import com.example.c424.utils.ad.AdUtil;
import com.example.c424.utils.ad.LaunchAppAdUtil;
import com.example.c424.utils.ad.ShowAdUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends Activity {
    @BindView(R.id.animationView)
    LottieAnimationView animationView;
    @BindView(R.id.privacyLayout)
    RelativeLayout privacyLayout;

    private Timer timer;
    private TimerTask timerTask;
    private int launchResult = -1;
    private int TIME_COUNT;
    private boolean isFirstLaunch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        if (SpUtil.isFirstLaunch(this)) {
            isFirstLaunch = true;
            privacyLayout.setVisibility(View.VISIBLE);
        } else {
            isFirstLaunch = false;
            privacyLayout.setVisibility(View.GONE);
            initTimer();
        }
    }

    private void initTimer() {
        int maxLaunchTime = SpUtil.getMaxSplashTime(SplashActivity.this);
        if (timer != null) {
            timer = null;
        }
        timer = new Timer();
        if (timerTask != null) {
            timerTask = null;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TIME_COUNT++;
                LogUtil.d("TIME_COUNT:" + TIME_COUNT + "    maxLaunchTime:" + maxLaunchTime);
                if (TIME_COUNT <= maxLaunchTime) {
                    if (launchResult == -1) {
                        launchResult = LaunchAppAdUtil.getLaunchPageResult();
                    } else {
                        if (TIME_COUNT >= 3 && TIME_COUNT <= maxLaunchTime) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //跳转或展示广告
                                    timer.cancel();
                                    if (isFirstLaunch && launchResult == Constant.Launch_Page_Show_Start && !SpUtil.getFistLaunchStartAdSwitch(SplashActivity.this)) {
                                        launchResult = Constant.Launch_Page_Into_Next;
                                    }
                                    processLaunchResult();
                                }
                            });
                        }
                    }
                } else if (TIME_COUNT > maxLaunchTime) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //跳转
                            timer.cancel();
                            launchResult = Constant.Launch_Page_Into_Next;
                            processLaunchResult();
                        }
                    });
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    @OnClick({R.id.closeBtn, R.id.agreeBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.restartPackage(getPackageName());
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                break;
            case R.id.agreeBtn:
                privacyLayout.setVisibility(View.GONE);
                SpUtil.setNotFirstLaunch(SplashActivity.this);
                AdUtil.getInstance().checkAllAdCache(SplashActivity.this);
                initTimer();
                break;
        }
    }

    private void processLaunchResult() {
        App.app.isNeedUpdateNativeAd = true;
        App.app.isNeedShowUpgradeDialog = true;
        if (launchResult == Constant.Launch_Page_Show_Start) {
            AdUtil.getInstance().setActivity(SplashActivity.this);
            if (App.app.hasIntoMain) {
                AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_FINISH);
                AdUtil.getInstance().setIntent(null);
            } else {
                AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_JUMP_FINISH);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("isFirstLaunch", isFirstLaunch);
                AdUtil.getInstance().setIntent(intent);
            }
            //
            ShowAdUtil.showStartAd(SplashActivity.this);
        } else if (launchResult == Constant.Launch_Page_Show_Finish) {
            AdUtil.getInstance().setActivity(SplashActivity.this);
            if (App.app.hasIntoMain) {
                AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_FINISH);
                AdUtil.getInstance().setIntent(null);
            } else {
                AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_JUMP_FINISH);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("isFirstLaunch", isFirstLaunch);
                AdUtil.getInstance().setIntent(intent);
            }
            //
            ShowAdUtil.showFinishAd(SplashActivity.this);
        } else if (launchResult == Constant.Launch_Page_Into_Next) {
            if (App.app.hasIntoMain) {
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("isFirstLaunch", isFirstLaunch);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}