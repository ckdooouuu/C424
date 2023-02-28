package com.example.c424.act;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.c424.App;
import com.example.c424.R;

import butterknife.ButterKnife;

public class FaqActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ButterKnife.bind(this);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.app.isNeedUpdateNativeAd = true;
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        App.app.isNeedUpdateNativeAd = true;
        super.onBackPressed();
    }
}
