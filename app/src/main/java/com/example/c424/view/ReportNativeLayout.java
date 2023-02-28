package com.example.c424.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.c424.R;

public class ReportNativeLayout extends FrameLayout {
    FrameLayout nativeAdContainer;

    public ReportNativeLayout(Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_report_native, this, false);
        nativeAdContainer = view.findViewById(R.id.nativeAdLayout);
        addView(view);
    }

    public FrameLayout getNativeAdContainer() {
        return nativeAdContainer;
    }
}
