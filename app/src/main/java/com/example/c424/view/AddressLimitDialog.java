package com.example.c424.view;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.c424.R;

public class AddressLimitDialog extends Dialog {
    private Context context;

    public AddressLimitDialog(@NonNull Context context) {
        super(context, R.style.dialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_address_limit_dialog);

        findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.restartPackage(context.getPackageName());
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        setCancelable(false);
    }
}
