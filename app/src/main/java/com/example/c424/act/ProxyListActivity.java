package com.example.c424.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c424.App;
import com.example.c424.Constant;
import com.example.c424.R;
import com.example.c424.adapter.ProxyListAdapter;
import com.example.c424.bean.BaseReqBean;
import com.example.c424.bean.ProxyCollect;
import com.example.c424.bean.ProxyInfo;
import com.example.c424.bean.ResponseBean;
import com.example.c424.network.Callback;
import com.example.c424.network.RequestManager;
import com.example.c424.utils.ConnectStatusUtil;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.PingUtil;
import com.example.c424.utils.SpUtil;
import com.example.c424.utils.ad.AdUtil;
import com.example.c424.utils.ad.ShowAdUtil;
import com.example.c424.view.MyRefreshHeader;
import com.example.c424.view.ProxyDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.blinkt.openvpn.util.VpnManage;

public class ProxyListActivity extends Activity {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.proxyRv)
    RecyclerView proxyRv;
    @BindView(R.id.fastestProxyCheckBtn)
    ImageView fastestProxyCheckBtn;

    MyRefreshHeader myRefreshHeader;

    private List<ProxyInfo> proxyInfoList = new ArrayList<>();
    private ProxyListAdapter adapter;

    private int selectedIndex = 0;
    private boolean isRefreshing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_list);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        selectedIndex = getIntent().getIntExtra("selectIndex", 0);

        if (selectedIndex != 0) {
            fastestProxyCheckBtn.setImageResource(R.mipmap.ic_uncheck);
        } else {
            fastestProxyCheckBtn.setImageResource(R.mipmap.ic_checked);
        }

        myRefreshHeader = new MyRefreshHeader(this);
        refreshLayout.setRefreshHeader(myRefreshHeader);

        proxyInfoList.addAll(App.app.proxyInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        proxyRv.setLayoutManager(linearLayoutManager);
        //
        adapter = new ProxyListAdapter(this);
        adapter.setSelectedIndex(selectedIndex);
        adapter.setProxyInfoList(proxyInfoList);
        proxyRv.setAdapter(adapter);
        adapter.setClickItemListener(new ProxyListAdapter.ClickItemListener() {
            @Override
            public void clickItem(int position) {
                if (!isRefreshing) {
                    if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
                        if (position != selectedIndex) {
                            //已连接情况下切换节点
                            ProxyDialog proxyDialog = new ProxyDialog(ProxyListActivity.this);
                            proxyDialog.show();
                            proxyDialog.setContent(String.format(getResources().getString(R.string.change_proxy_content), proxyInfoList.get(selectedIndex).getServerCouAbbr(), proxyInfoList.get(position).getServerCouAbbr()));
                            proxyDialog.setPositiveBtn(getResources().getString(R.string.reconnect2));
                            proxyDialog.setClickPositiveListener(new ProxyDialog.ClickPositiveListener() {
                                @Override
                                public void clickPositive() {
                                    selectedIndex = position;
                                    backToMain(5001);
                                }
                            });
                        }
                    } else if (ConnectStatusUtil.getInstance().getStatus() == Constant.UN_CONNECT) {
                        //未连接情况下，选择节点
                        selectedIndex = position;

                        if (selectedIndex != 0) {
                            fastestProxyCheckBtn.setImageResource(R.mipmap.ic_uncheck);
                        } else {
                            fastestProxyCheckBtn.setImageResource(R.mipmap.ic_checked);
                        }

                        adapter.setSelectedIndex(position);
                        adapter.notifyDataSetChanged();
                        //返回到主页连接
                        backToMain(5002);
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                readyRefreshList();
            }
        });

        PingUtil.getINSTANCE().setPingFinishListener(new PingUtil.PingFinishListener() {
            @Override
            public void pingFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d("ProxyListActivity ping finish");
                        if (App.app.proxyInfoList != null && App.app.proxyInfoList.size() > 0) {
                            proxyInfoList.clear();
                            adapter.notifyDataSetChanged();
                            proxyInfoList.addAll(App.app.proxyInfoList);
                            adapter.notifyDataSetChanged();
                        }
                        selectedIndex = 0;
                        isRefreshing = false;

                        fastestProxyCheckBtn.setImageResource(R.mipmap.ic_checked);
                        adapter.setSelectedIndex(0);
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isRefreshing) {
            showExtraAd();
        }
    }


    @OnClick({R.id.backBtn, R.id.refreshBtn, R.id.fastestProxyLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                if (!isRefreshing) {
                    showExtraAd();
                }
                break;
            case R.id.refreshBtn:
                if (!isRefreshing) {
                    readyRefreshList();
                }
                break;
            case R.id.fastestProxyLayout:
                if (!isRefreshing) {
                    fastestProxyCheckBtn.setImageResource(R.mipmap.ic_checked);
                    if (ConnectStatusUtil.getInstance().getStatus() == Constant.UN_CONNECT) {
                        selectedIndex = 0;
                        adapter.setSelectedIndex(0);
                        adapter.notifyDataSetChanged();
                        //返回到主页连接
                        backToMain(5002);
                    } else {
                        if (selectedIndex != 0) {
                            //已连接情况下切换节点
                            ProxyDialog proxyDialog = new ProxyDialog(ProxyListActivity.this);
                            proxyDialog.show();
                            proxyDialog.setContent(String.format(getResources().getString(R.string.change_proxy_content), proxyInfoList.get(selectedIndex).getServerCouAbbr(), proxyInfoList.get(0).getServerCouAbbr()));
                            proxyDialog.setPositiveBtn(getResources().getString(R.string.reconnect2));
                            proxyDialog.setClickPositiveListener(new ProxyDialog.ClickPositiveListener() {
                                @Override
                                public void clickPositive() {
                                    selectedIndex = 0;
                                    backToMain(5001);
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    private void getProxyList() {
        String androidID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        BaseReqBean baseReqBean = new BaseReqBean(getPackageName(), App.app.SDK_VERSION, androidID, App.app.googleID,
                Build.VERSION.SDK_INT, "en", App.app.countryName, System.currentTimeMillis(), "",
                App.app.simCountry, App.app.firstInstallTime);
        RequestManager.getProxyList(baseReqBean, new Callback(new Callback.OnCallbackListener() {
            @Override
            public void onError(String errorMsg) {
                LogUtil.d("getProxyList errorMsg:" + errorMsg);
            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                ProxyCollect proxyCollect = (ProxyCollect) responseBean.getRespData();
//                LogUtil.d("getProxyList Response1:" + proxyCollect.getVsArrays());
//                LogUtil.d("getProxyList Response2:" + proxyCollect.getServerArrayPro());
                PingUtil.getINSTANCE().pingProxy(ProxyListActivity.this, proxyCollect.getVsArrays());
            }
        }));
    }

    private void readyRefreshList() {
        if (ConnectStatusUtil.getInstance().getStatus() == Constant.CONNECTED) {
            ProxyDialog proxyDialog = new ProxyDialog(this);
            proxyDialog.show();
            proxyDialog.setContent(getResources().getString(R.string.refresh_warning));
            proxyDialog.setPositiveBtn(getResources().getString(R.string.refresh));
            proxyDialog.setClickPositiveListener(new ProxyDialog.ClickPositiveListener() {
                @Override
                public void clickPositive() {
                    isRefreshing = true;
                    VpnManage.getInstance().stopVpn(ProxyListActivity.this);
                    ConnectStatusUtil.getInstance().setStatus(Constant.UN_CONNECT);
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.autoRefresh();
                            getProxyList();
                        }
                    }, 200);
                }
            });
            proxyDialog.setClickNegativeListener(new ProxyDialog.ClickNegativeListener() {
                @Override
                public void clickNegative() {
                    refreshLayout.finishRefresh();
                }
            });
        } else {
            isRefreshing = true;
            refreshLayout.autoRefresh();
            getProxyList();
        }
    }

    /**
     * 切换节点、点击连接节点、点击返回
     */
    private void backToMain(int resultCode) {
        App.app.isNeedUpdateNativeAd = true;
        Intent intent = new Intent();
        intent.putExtra("CurrentSelectedProxyIndex", selectedIndex);
        setResult(resultCode, intent);
        finish();
    }

    private void showExtraAd() {
        App.app.isNeedUpdateNativeAd = true;
        if (SpUtil.getBackFromProxyListAdSwitch(this)) {
            AdUtil.getInstance().setActivity(ProxyListActivity.this);
            AdUtil.getInstance().setCloseAdAction(Constant.CLOSE_AD_ACTION_FINISH);
            Intent intent = new Intent();
            intent.putExtra("CurrentSelectedProxyIndex", selectedIndex);
            setResult(5003, intent);
            AdUtil.getInstance().setIntent(null);
            ShowAdUtil.showFinishAd(ProxyListActivity.this);
        } else {
            finish();
        }
    }

}
