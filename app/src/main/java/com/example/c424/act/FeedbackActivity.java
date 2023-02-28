package com.example.c424.act;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.c424.App;
import com.example.c424.R;
import com.example.c424.bean.ReqFeedback;
import com.example.c424.bean.ResponseBean;
import com.example.c424.network.Callback;
import com.example.c424.network.RequestManager;
import com.example.c424.utils.LogUtil;
import com.example.c424.utils.NetworkUtil;
import com.example.c424.view.FeedbackDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends Activity {
    @BindView(R.id.feedbackTypeGroup)
    RadioGroup feedbackTypeGroup;
    @BindView(R.id.sendBtn)
    TextView sendBtn;
    @BindView(R.id.problemDesc)
    EditText problemDesc;
    @BindView(R.id.typeTv)
    TextView typeTv;

    private String type;
    FeedbackDialog feedbackDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);

        feedbackTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sendBtn.setBackground(getResources().getDrawable(R.drawable.bg_gradient_btn));
                RadioButton btn = (RadioButton) findViewById(checkedId);
                if (btn != null) {
                    type = btn.getText().toString();
                    sendBtn.setClickable(true);
                    sendBtn.setEnabled(true);
                }
            }
        });

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.feedback_type));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        spannableString.setSpan(foregroundColorSpan, spannableString.length() - 2, spannableString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        typeTv.setText(spannableString);
    }

    @Override
    public void onBackPressed() {
        if (feedbackDialog != null) {
            if (feedbackDialog.isShowing()) {
                feedbackDialog.dismiss();
            }
        }
        finish();
    }

    @OnClick({R.id.backBtn, R.id.sendBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                feedbackTypeGroup.clearCheck();
                if (feedbackDialog != null) {
                    if (feedbackDialog.isShowing()) {
                        feedbackDialog.dismiss();
                    }
                }
                finish();
                break;
            case R.id.sendBtn:
                checkNetworkUse();
                break;
        }
    }

    private void checkNetworkUse() {
        sendBtn.setClickable(false);
        sendBtn.setEnabled(false);
        boolean isAvailable = NetworkUtil.checkNetworkAvailable(this);
        if (isAvailable) {
            uploadFeedback();
        } else {
            feedbackDialog = new FeedbackDialog(this, false);
            feedbackDialog.show();
            feedbackDialog.setClickPositiveListener(new FeedbackDialog.ClickPositiveListener() {
                @Override
                public void onClick() {
                    sendBtn.setClickable(true);
                    sendBtn.setEnabled(true);
                    checkNetworkUse();
                }
            });
        }
    }

    private void uploadFeedback() {
        ReqFeedback reqFeedback = new ReqFeedback();
        reqFeedback.setBundle_id(getPackageName());
        reqFeedback.setCountry(App.app.countryName);
        reqFeedback.setTitlesFeed(type);
        reqFeedback.setQuestions(problemDesc.getText().toString());
        RequestManager.feedback(reqFeedback, new Callback(new Callback.OnCallbackListener() {
            @Override
            public void onError(String errorMsg) {
                LogUtil.d("uploadFeedback errorMsg:" + errorMsg);

                feedbackDialog = new FeedbackDialog(FeedbackActivity.this, false);
                feedbackDialog.show();
                feedbackDialog.setClickPositiveListener(new FeedbackDialog.ClickPositiveListener() {
                    @Override
                    public void onClick() {
                        sendBtn.setClickable(true);
                        sendBtn.setEnabled(true);
                        checkNetworkUse();
                    }
                });
            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                LogUtil.d("uploadFeedback response:" + responseBean.toString());
                if (responseBean.getReturnCode() == 200) {
                    sendBtn.setClickable(true);
                    sendBtn.setEnabled(true);
                    feedbackDialog = new FeedbackDialog(FeedbackActivity.this, true);
                    feedbackDialog.show();
                    feedbackDialog.setClickPositiveListener(new FeedbackDialog.ClickPositiveListener() {
                        @Override
                        public void onClick() {
                            sendBtn.setClickable(true);
                            sendBtn.setEnabled(true);
                            sendBtn.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 200);
                        }
                    });
                } else {
                    feedbackDialog = new FeedbackDialog(FeedbackActivity.this, false);
                    feedbackDialog.show();
                    feedbackDialog.setClickPositiveListener(new FeedbackDialog.ClickPositiveListener() {
                        @Override
                        public void onClick() {

                            sendBtn.setClickable(true);
                            sendBtn.setEnabled(true);
                            checkNetworkUse();
                        }
                    });
                }
            }
        }));
    }
}
