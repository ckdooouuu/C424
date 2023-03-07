package com.example.c424.act;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.c424.App;
import com.example.c424.BuildConfig;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.bean.ConnectType;
import com.example.c424.bean.ConnectionProxyInfo;
import com.example.c424.bean.ConnectionResult;
import com.example.c424.bean.GlobalConfig;
import com.example.c424.bean.ProxyInfo;
import com.example.c424.bean.ReqUploadConnectionResult;
import com.example.c424.bean.ResponseBean;
import com.example.c424.bean.UpdateConfig;
import com.example.c424.network.Callback;
import com.example.c424.network.RequestManager;
import com.example.c424.utils.AssetsUtil;
import com.example.c424.utils.ConnectStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.NetworkUtil;
import com.example.c424.utils.PingUtil;
import com.example.c424.utils.SpUtil;
import com.example.c424.utils.ad.AdUtil;
import com.example.c424.utils.ad.ShowAdUtil;
import com.example.c424.view.ProxyDialog;
import com.example.c424.view.UpgradeDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.network.crypt.NativeLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.VpnStatus;
import de.blinkt.openvpn.util.ProxyModel;
import de.blinkt.openvpn.util.VpnManage;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.firstFlag)
    ImageView firstFlag;
    @BindView(R.id.secondFlag)
    ImageView secondFlag;
    @BindView(R.id.currentProxyFlag)
    ImageView currentProxyFlag;
    @BindView(R.id.thirdFlag)
    ImageView thirdFlag;
    @BindView(R.id.fourthFlag)
    ImageView fourthFlag;
    @BindView(R.id.currentProxyName)
    TextView currentProxyName;
    @BindView(R.id.unConnectAnimation)
    LinearLayout unConnectAnimation;
    @BindView(R.id.connectingAnimation)
    LinearLayout connectingAnimation;
    @BindView(R.id.connectedAnimation)
    LinearLayout connectedAnimation;
    @BindView(R.id.nativeAdLayout)
    FrameLayout nativeAdLayout;
    @BindView(R.id.bannerAdLayout)
    FrameLayout bannerAdLayout;
    @BindView(R.id.guideMask)
    View guideMask;
    @BindView(R.id.guideLayout)
    RelativeLayout guideLayout;
    @BindView(R.id.closeGuideBtn)
    ImageView closeGuideBtn;
    @BindView(R.id.guideIcon)
    ImageView guideIcon;

    private ProxyInfo currentProxyInfo;
    private ImageView[] flagArray;

    private volatile static int currentConnectStrategyIndex = 0;//当前连接策略的下标
    private List<ConnectType> connectStrategyList = new ArrayList<>();//连接策略集合
    private int currentConnectStrategy;
    private String currentConnectPort;
    private ProxyDialog interruptDialog;
    private ProxyDialog disconnectDialog;
    private UpgradeDialog upgradeDialog;
    private static long startConnectTimeStamp;
    private int currentSelectedProxyIndex = 0;
    private boolean continueConnect = false;//切换连接策略的时候需要中断连接，监听的状态会到ConnectionStatus.LEVEL_NOTCONNECTED，这样的话连接动画会暂停，当该变量为true时，

    private Handler handler = new Handler();

    //单次连接超时
    private Runnable singleConnectTimeout = new Runnable() {
        @Override
        public void run() {
            uploadConnectResult(1, currentConnectStrategy, currentConnectPort);
            judgeIsReconnectByLastStrategy();
        }
    };
    //最大连接时长
    private Runnable maxConnectedTime = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("stop connect ");
            stopConnect();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConnectListener();
        ButterKnife.bind(this);

        boolean isFirstLaunch = getIntent().getBooleanExtra("isFirstLaunch", false);
        boolean isShowGuide = SpUtil.isShowConnectGuide(this);
        if (isFirstLaunch && isShowGuide) {
            guideMask.setVisibility(View.VISIBLE);
            guideLayout.setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation = new TranslateAnimation(-20, 0, -20, 0);
            translateAnimation.setDuration(500);
            translateAnimation.setRepeatCount(-1);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            guideIcon.startAnimation(translateAnimation);
        }

        App.app.hasIntoMain = true;

        ConnectStatusUtil.getInstance().setConnectStatusChangeListener(new ConnectStatusUtil.ConnectStatusChangeListener() {
            @Override
            public void connectStatusChange(int s) {
                if (Constant.UN_CONNECT == s) {
                    unConnectAnimation.setVisibility(View.VISIBLE);
                    connectingAnimation.setVisibility(View.GONE);
                    connectedAnimation.setVisibility(View.GONE);
                    handler.removeCallbacks(singleConnectTimeout);
                    handler.removeCallbacks(maxConnectedTime);
                } else if (Constant.CONNECTING == s) {
                    unConnectAnimation.setVisibility(View.GONE);
                    connectingAnimation.setVisibility(View.VISIBLE);
                    connectedAnimation.setVisibility(View.GONE);
                } else if (Constant.CONNECTED == s) {
                    unConnectAnimation.setVisibility(View.GONE);
                    connectingAnimation.setVisibility(View.GONE);
                    connectedAnimation.setVisibility(View.VISIBLE);
                    handler.postDelayed(maxConnectedTime, SpUtil.getMaxConnectTime(MainActivity.this) * 60 * 1000);
                }
            }
        });

        ConnectStatusUtil.getInstance().setStatus(Constant.UN_CONNECT);
        flagArray = new ImageView[]{firstFlag, secondFlag, thirdFlag, fourthFlag};
        initProxyView();


        PingUtil.getINSTANCE().setPingFinishListener(new PingUtil.PingFinishListener() {
            @Override
            public void pingFinish() {
                LogUtil.d("MainActivity ping finish");
                initProxyView();
            }
        });


        initFlag(firstFlag, "BG");
        initFlag(secondFlag, "BR");
        initFlag(thirdFlag, "IE");
        initFlag(fourthFlag, "IL");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.app.isNeedUpdateNativeAd) {
            App.app.isNeedUpdateNativeAd = false;
            ShowAdUtil.showHomeAd(MainActivity.this, nativeAdLayout, bannerAdLayout);
        }
        if (App.app.isNeedShowUpgradeDialog) {
            App.app.isNeedShowUpgradeDialog = false;
            UpdateConfig updateConfig = SpUtil.getUpdateConfig(this);
            if (updateConfig != null) {
                if (updateConfig.getMinApp() > BuildConfig.VERSION_CODE) {
                    upgradeDialog = new UpgradeDialog(this);
                    upgradeDialog.show();
                    upgradeDialog.isForce(updateConfig.isMustForce());
                    upgradeDialog.setTitle(updateConfig.getUpgradett());
                    upgradeDialog.setContent(updateConfig.getDialogMsg());
                    upgradeDialog.setClickUpgradeBtnListener(new UpgradeDialog.ClickUpgradeBtnListener() {
                        @Override
                        public void clickUpgradeBtn() {
                            upgradeDialog.dismiss();
                            String url = "http://play.google.com/store/apps/details?id=" + updateConfig.getUpgradeName();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }
                    });
                }
            }

            //TODO zjh
//            if ("CN".equals(App.app.countryName.toUpperCase())) {
//                AddressLimitDialog addressLimitDialog = new AddressLimitDialog(this);
//                addressLimitDialog.show();
//            }
        }
    }

    private void initConnectListener() {
        VpnStatus.connectStatusListener = new VpnStatus.ConnectStatusListener() {
            @Override
            public void connectStatus(ConnectionStatus level) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d("MainActivity connect status:" + level);
                        if (level == ConnectionStatus.LEVEL_CONNECTED) {
                            LogUtil.d("连接成功");
                            ConnectStatusUtil.getInstance().setStatus(Constant.CONNECTED);
                            handler.removeCallbacks(singleConnectTimeout);

                            //上报连接结果
                            LogUtil.d("currentConnectPort:" + currentConnectPort);
                            uploadConnectResult(0, currentConnectStrategy, currentConnectPort);

                            jumpToReport(true);
                        } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                            LogUtil.d("continueConnect:" + continueConnect);
                            if (continueConnect) {
                                continueConnect = false;
                            } else {
                                if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTING) {
                                    LogUtil.d("连接失败");
                                    handler.removeCallbacks(singleConnectTimeout);
                                    judgeIsReconnectByLastStrategy();

                                    //上报连接结果
                                    uploadConnectResult(1, currentConnectStrategy, currentConnectPort);
                                }
                            }
                        }
                    }
                });
            }
        };
    }

    @OnClick({R.id.settingBtn, R.id.speedBtn, R.id.locationBtn, R.id.faqBtn, R.id.currentProxyFlag,
            R.id.unConnectAnimation, R.id.connectingAnimation, R.id.connectedAnimation, R.id.closeGuideBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settingBtn:
                if (ConnectStatusUtil.getInstance().getStatus() != Constant.CONNECTING) {
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                }
                break;
            case R.id.speedBtn:
                if (ConnectStatusUtil.getInstance().getStatus() != Constant.CONNECTING) {
                    App.app.isNeedUpdateNativeAd = true;
                    startActivityForResult(new Intent(MainActivity.this, SpeedActivity.class), 6000);
                }
                break;
            case R.id.locationBtn:
                if (ConnectStatusUtil.getInstance().getStatus() != Constant.CONNECTING) {
                    App.app.isNeedUpdateNativeAd = true;
                    startActivityForResult(new Intent(MainActivity.this, LocationActivity.class), 7000);
                }
                break;
            case R.id.faqBtn:
                if (ConnectStatusUtil.getInstance().getStatus() != Constant.CONNECTING) {
                    startActivity(new Intent(MainActivity.this, FaqActivity.class));
                }
                break;
            case R.id.currentProxyFlag:
                if (ConnectStatusUtil.getInstance().getStatus() != Constant.CONNECTING) {
                    Intent intent = new Intent(MainActivity.this, ProxyListActivity.class);
                    intent.putExtra("selectIndex", currentSelectedProxyIndex);
                    startActivityForResult(intent, 5000);
                }
                break;
            case R.id.unConnectAnimation:
                if (App.app.proxyCountryList != null && App.app.proxyCountryList.size() > 0 && currentProxyInfo != null) {
                    checkVPNPermission();
                    guideMask.setVisibility(View.GONE);
                    guideLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.connectingAnimation:
                if (interruptDialog == null) {
                    interruptDialog = new ProxyDialog(MainActivity.this);
                }
                interruptDialog.show();
                interruptDialog.setContent(getResources().getString(R.string.ask_interrupt_connect));
                interruptDialog.setPositiveBtn(getResources().getString(R.string.interrupt));
                interruptDialog.setClickPositiveListener(new ProxyDialog.ClickPositiveListener() {
                    @Override
                    public void clickPositive() {
                        stopConnect();
                    }
                });
                break;
            case R.id.connectedAnimation:
                if (disconnectDialog == null) {
                    disconnectDialog = new ProxyDialog(MainActivity.this);
                }
                disconnectDialog.show();
                disconnectDialog.setContent(getResources().getString(R.string.ask_disconnect));
                disconnectDialog.setPositiveBtn(getResources().getString(R.string.disconnect));
                disconnectDialog.setClickPositiveListener(new ProxyDialog.ClickPositiveListener() {
                    @Override
                    public void clickPositive() {
                        stopConnect();
                        jumpToReport(false);
                    }
                });
                break;
            case R.id.closeGuideBtn:
                guideMask.setVisibility(View.GONE);
                guideLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void initFlag(ImageView view, String countryName) {
        Bitmap flag2 = AssetsUtil.getFlag(MainActivity.this, countryName);
        if (flag2 == null) {
            flag2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nor);
        }
        Glide.with(MainActivity.this)
                .load(flag2)
                .into(new SimpleTarget<Drawable>() {//这样处理，刷新列表的时候图片就不会闪烁
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                        view.setImageDrawable(resource);
                    }
                });
    }

    private void initProxyView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (App.app.proxyInfoList != null && App.app.proxyInfoList.size() > 0) {
                    currentProxyInfo = App.app.proxyInfoList.get(currentSelectedProxyIndex);
                    Bitmap flag = AssetsUtil.getFlag(MainActivity.this, currentProxyInfo.getServerCouAbbr().toUpperCase());
                    if (flag == null) {
                        flag = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nor);
                    }
                    currentProxyName.setText(currentProxyInfo.getServerGN());
                    Glide.with(MainActivity.this)
                            .load(flag)
                            .into(new SimpleTarget<Drawable>() {//这样处理，刷新列表的时候图片就不会闪烁
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                    currentProxyFlag.setImageDrawable(resource);
                                }
                            });
                    //
                    //
                    //
                    LogUtil.d("country size:" + App.app.proxyCountryList.size());
                    final int[] currentSetFlag = {0};
                    if (App.app.proxyCountryList != null && App.app.proxyCountryList.size() > 0 && flagArray != null && flagArray.length > 0) {
                        for (int i = 0; i < App.app.proxyCountryList.size(); i++) {
                            if (i > 3 - currentSetFlag[0]) {//如果国家下标大于3，说明已经所有国旗图片设置完成

                                return;
                            }
                            if (!App.app.proxyCountryList.get(i).toUpperCase().equals(currentProxyInfo.getServerCouAbbr().toUpperCase())) {
                                Bitmap flag2 = AssetsUtil.getFlag(MainActivity.this, App.app.proxyCountryList.get(i));
                                if (flag2 == null) {
                                    flag2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_nor);
                                }
                                Glide.with(MainActivity.this)
                                        .load(flag2)
                                        .into(new SimpleTarget<Drawable>() {//这样处理，刷新列表的时候图片就不会闪烁
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                                flagArray[currentSetFlag[0]].setImageDrawable(resource);
                                                currentSetFlag[0]++;
                                            }
                                        });
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        });
    }

    private void checkVPNPermission() {
        continueConnect = false;
        currentConnectStrategyIndex = 0;
        currentConnectPort = "";
        ConnectStatusUtil.getInstance().setStatus(Constant.CONNECTING);
        connectStrategyList.clear();
        connectStrategyList.addAll(SpUtil.getConnectType(MainActivity.this));
        //
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            Log.d("iVPN", "intent != null");
            try {
                startActivityForResult(intent, 95270);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
            }
        } else {
            Log.d("zjh", "intent == null");
            startConnect();
        }
    }

    /**
     * 只有连接失败和连接超时才需要这样做
     */
    private void judgeIsReconnectByLastStrategy() {
        currentConnectStrategyIndex++;
        continueConnect = true;
        VpnManage.getInstance().stopVpn(this);

        LogUtil.d("下一个连接策略下标：" + currentConnectStrategyIndex + "    连接策略size：" + connectStrategyList.size());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentConnectStrategyIndex < connectStrategyList.size()) {
                    LogUtil.d("使用下一个连接策略进行连接");
                    startConnect();
                } else {
                    LogUtil.d("所有连接策略已使用完毕，为连接成功，结果为连接失败");
                    continueConnect = false;
                    stopConnect();
                    jumpToReport(false);
                }
            }
        }, 200);

    }

    private void startConnect() {
        startConnectTimeStamp = System.currentTimeMillis();
        handler.postDelayed(singleConnectTimeout, connectStrategyList.get(currentConnectStrategyIndex).getTimeouts() * 1000);
        ProxyModel proxyModel = getConnectProxyInfo();
        LogUtil.d("proxyModel:" + proxyModel.toString());
        VpnManage.getInstance().startVpn(MainActivity.this, proxyModel, SpUtil.getBlackList(this));
    }

    private void stopConnect() {
        VpnManage.getInstance().stopVpn(this);
        ConnectStatusUtil.getInstance().setStatus(Constant.UN_CONNECT);
    }

    private ProxyModel getConnectProxyInfo() {
        ProxyModel proxyModel = new ProxyModel();

        proxyModel.setIp(currentProxyInfo.getHostServer());
        //
        String proxyUserName = currentProxyInfo.getUserName();
        String proxyPassword = currentProxyInfo.getOwnerPwd();
        List<String> tcpPortList = new ArrayList<>();
        List<String> udpPortList = new ArrayList<>();

        String portStr;
        if (proxyUserName == null || proxyUserName.isEmpty() || proxyPassword == null || proxyPassword.isEmpty()) {
            portStr = currentProxyInfo.getOvports();
        } else {
            proxyModel.setUserName(proxyUserName);
            proxyModel.setPassword(proxyPassword);
            portStr = currentProxyInfo.getCryptport();
        }
        String[] port1Array = portStr.split(",");
        for (int i = 0; i < port1Array.length; i++) {
            if ("TCP".equals(port1Array[i].split(":")[0].toUpperCase())) {
                tcpPortList.add(port1Array[i].split(":")[1]);
            } else if ("UDP".equals(port1Array[i].split(":")[0].toUpperCase())) {
                udpPortList.add(port1Array[i].split(":")[1]);
            }
        }

        if (connectStrategyList.get(currentConnectStrategyIndex).getStrategyType() == 1) {
            //使用tcp端口
            proxyModel.setPort(getRandomPort(tcpPortList));
            currentConnectStrategy = 2;
            proxyModel.setUseUDP(false);
        } else if (connectStrategyList.get(currentConnectStrategyIndex).getStrategyType() == 2) {
            //使用udp端口
            currentConnectStrategy = 1;
            proxyModel.setPort(getRandomPort(udpPortList));
            proxyModel.setUseUDP(true);
        }
        return proxyModel;
    }

    private String getRandomPort(List<String> portList) {
        Random random = new Random();
        if (portList != null && portList.size() > 0) {
            int index = random.nextInt(portList.size());
            String port = portList.get(index);
            if (currentConnectPort != null && !currentConnectPort.isEmpty()) {
                if (currentConnectPort.equals(port)) {
                    getRandomPort(portList);
                } else {
                    currentConnectPort = port;
                }
            } else {
                currentConnectPort = port;
            }
        }
        return currentConnectPort;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5000) {//列表页
            if (App.app.proxyCountryList != null && App.app.proxyCountryList.size() > 0) {
                if (data != null) {
                    //需要带回 当前节点的下标
                    currentSelectedProxyIndex = data.getIntExtra("CurrentSelectedProxyIndex", 0);
                    currentProxyInfo = App.app.proxyInfoList.get(currentSelectedProxyIndex);
                    if (resultCode == 5001 || resultCode == 5002) {//5001切换连接节点   5002点击节点进行连接
                        stopConnect();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkVPNPermission();
                            }
                        }, 200);
                    } else if (resultCode == 5003) {//普通返回
                    }
                }
                initProxyView();//当在列表页刷新节点列表，需要刷新首页UI
                PingUtil.getINSTANCE().setPingFinishListener(new PingUtil.PingFinishListener() {
                    @Override
                    public void pingFinish() {
                        LogUtil.d("MainActivity ping finish");
                        initProxyView();
                    }
                });
            }
        } else if (requestCode == 95270) {//VPN连接权限
            LogUtil.d("resultCode:" + resultCode);
            if (resultCode == 0) {//VPN权限未被允许

            } else if (resultCode == -1) {//VPN权限已允许
                startConnect();
            }
        } else if (requestCode == 6000) {//测速页面
            if (resultCode == 6001 && App.app.proxyCountryList != null && App.app.proxyCountryList.size() > 0) {
                currentSelectedProxyIndex = 0;
                currentProxyInfo = App.app.proxyInfoList.get(currentSelectedProxyIndex);
                initProxyView();//当在列表页刷新节点列表，需要刷新首页UI
                checkVPNPermission();
            }
        } else if (requestCode == 7000) {//定位页
            if (resultCode == 7001 && App.app.proxyCountryList != null && App.app.proxyCountryList.size() > 0) {
                currentSelectedProxyIndex = 0;
                currentProxyInfo = App.app.proxyInfoList.get(currentSelectedProxyIndex);
                initProxyView();//当在列表页刷新节点列表，需要刷新首页UI
                checkVPNPermission();
            }
        }
    }

    private void jumpToReport(boolean isSuccess) {
        if (interruptDialog != null) {
            if (interruptDialog.isShowing()) {
                interruptDialog.dismiss();
            }
        }
        if (disconnectDialog != null) {
            if (disconnectDialog.isShowing()) {
                disconnectDialog.dismiss();
            }
        }
        showFinishAd(isSuccess);
    }

    private void showFinishAd(boolean isSuccess) {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("isSuccess", isSuccess);
        intent.putExtra("country", currentProxyInfo.getWeb_cou());
        intent.putExtra("alias", currentProxyInfo.getWebAlias());
        intent.putExtra("flag", currentProxyInfo.getServerCouAbbr());
        AdUtil.getInstance().setActivity(MainActivity.this);
        AdUtil.getInstance().setIntent(intent);
        AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_JUMP);
        ShowAdUtil.showFinishAd(MainActivity.this);
    }

    /**
     * @param result 0成功   1失败
     */
    private void uploadConnectResult(int result, int connectStrategy, String port) {
        LogUtil.d("port::::::" + port);
        long connectInterval = System.currentTimeMillis() - startConnectTimeStamp;
        //
        ConnectionProxyInfo connectionProxyInfo = new ConnectionProxyInfo();
        connectionProxyInfo.setAuto(connectStrategy);
        connectionProxyInfo.setP_link((int) currentProxyInfo.getDelay());
        connectionProxyInfo.setProxIp(currentProxyInfo.getHostServer());
        connectionProxyInfo.setCouServers(currentProxyInfo.getWeb_cou());
        connectionProxyInfo.setWebAlias(currentProxyInfo.getWebAlias());
        //
        ConnectionResult connectionResult = new ConnectionResult();
        connectionResult.setPoint(Integer.parseInt(port));
        connectionResult.setLinkStatus(result);
        connectionResult.setLinkTimes(connectInterval);
        connectionResult.setJoinType(connectStrategy);
        //
        List<ConnectionResult> connectionResultList = new ArrayList<ConnectionResult>();
        connectionResultList.add(connectionResult);
        connectionProxyInfo.setKnotInfo(connectionResultList);
        //
//        reqUploadConnectionResult.setServerInfo(connectionProxyInfo);

        String androidID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        ReqUploadConnectionResult reqUploadConnectionResult = new ReqUploadConnectionResult(getPackageName(), App.app.SDK_VERSION, androidID, App.app.googleID,
                Build.VERSION.SDK_INT, "en", App.app.countryName, System.currentTimeMillis(), "",
                App.app.simCountry, App.app.firstInstallTime, NetworkUtil.getIp(this), connectionProxyInfo);

        reqUploadConnectionResult.setUserIp(NetworkUtil.getIp(this));

        LogUtil.d("reqUploadConnectionResult:" + reqUploadConnectionResult);
        RequestManager.uploadConnectResult(reqUploadConnectionResult, new Callback(new Callback.OnCallbackListener() {
            @Override
            public void onError(String errorMsg) {
                LogUtil.d("uploadConnectResult errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                LogUtil.d("uploadConnectResult success: " + responseBean.toString());
            }
        }));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (App.app.isBackground) {
            if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTING) {
                stopConnect();
            }
            if (interruptDialog != null) {
                if (interruptDialog.isShowing()) {
                    interruptDialog.dismiss();
                }
            }
            if (disconnectDialog != null) {
                if (disconnectDialog.isShowing()) {
                    disconnectDialog.dismiss();
                }
            }
        }
    }
}