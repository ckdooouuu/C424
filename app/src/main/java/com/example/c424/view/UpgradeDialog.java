package com.example.c424.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.c424.R;

public class UpgradeDialog extends Dialog {
    TextView title;
    TextView content;
    GradientFrame cancelBtn;
    TextView upgradeBtn;
    private ClickUpgradeBtnListener clickUpgradeBtnListener;

    public UpgradeDialog(@NonNull Context context) {
        super(context, R.style.dialogTheme);
    }

    public void setClickUpgradeBtnListener(ClickUpgradeBtnListener clickUpgradeBtnListener) {
        this.clickUpgradeBtnListener = clickUpgradeBtnListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_upgrade_dialog);

        setCancelable(false);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        cancelBtn = findViewById(R.id.cancelBtn);
        upgradeBtn = findViewById(R.id.upgradeBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickUpgradeBtnListener != null) {
                    clickUpgradeBtnListener.clickUpgradeBtn();
                }
            }
        });
    }

    public void setTitle(String str) {
        title.setText(str);
    }

    public void setContent(String str) {
        content.setText(str);
    }

    public void isForce(boolean isForce) {
        if (isForce) {
            cancelBtn.setVisibility(View.GONE);
        }
    }

    public interface ClickUpgradeBtnListener {
        void clickUpgradeBtn();
    }
}
