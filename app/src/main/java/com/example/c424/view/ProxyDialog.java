package com.example.c424.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.c424.R;

public class ProxyDialog extends Dialog {
    private TextView content;
    private GradientFrame negativeBtn;
    private TextView positiveBtn;
    private ClickPositiveListener clickPositiveListener;
    private ClickNegativeListener clickNegativeListener;

    public ProxyDialog(@NonNull Context context) {
        super(context, R.style.dialogTheme);
    }

    public void setClickPositiveListener(ClickPositiveListener clickPositiveListener) {
        this.clickPositiveListener = clickPositiveListener;
    }

    public void setClickNegativeListener(ClickNegativeListener clickNegativeListener) {
        this.clickNegativeListener = clickNegativeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_proxy_dialog);

        content = findViewById(R.id.content);
        negativeBtn = findViewById(R.id.negativeBtn);
        positiveBtn = findViewById(R.id.positiveBtn);

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickNegativeListener != null) {
                    clickNegativeListener.clickNegative();
                }
            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickPositiveListener != null) {
                    clickPositiveListener.clickPositive();
                }
            }
        });
    }

    public void setContent(String str) {
        content.setText(str);
    }

//    public void setNegativeBtn(String str) {
//        negativeBtn.setText(str);
//    }

    public void setPositiveBtn(String str) {
       positiveBtn.setText(str);
    }

    public interface ClickPositiveListener {
        void clickPositive();
    }

    public interface ClickNegativeListener {
        void clickNegative();
    }
}
