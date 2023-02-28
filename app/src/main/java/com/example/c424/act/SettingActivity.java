package com.example.c424.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.c424.App;
import com.example.c424.BuildConfig;
import com.example.c424.R;
import com.example.c424.view.RateDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {
    @BindView(R.id.versionNameTv)
    TextView versionNameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        versionNameTv.setText(String.format(getResources().getString(R.string.version), BuildConfig.VERSION_NAME));
    }

    @OnClick({R.id.backBtn, R.id.rateUsLayout, R.id.shareUsLayout, R.id.feedbackLayout, R.id.privacyLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                App.app.isNeedUpdateNativeAd = true;
                finish();
                break;
            case R.id.rateUsLayout:
                RateDialog rateDialog = new RateDialog(SettingActivity.this);
                rateDialog.show();
                rateDialog.setCancelable(true);
                break;
            case R.id.shareUsLayout:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                String pkgName = this.getPackageName();
                String url = "http://play.google.com/store/apps/details?id=" + pkgName;
                intent.putExtra(Intent.EXTRA_TEXT, url);
                intent.setType("text/plain");
                startActivity(intent);
                break;
            case R.id.feedbackLayout:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;
            case R.id.privacyLayout:
                startActivity(new Intent(SettingActivity.this, PrivacyActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
