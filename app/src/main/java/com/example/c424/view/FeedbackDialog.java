package com.example.c424.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.c424.R;

public class FeedbackDialog extends Dialog {
    private boolean isSuccess;
    private TextView content;
    private GradientFrame negativeBtn;
    private TextView positiveBtn;
    private Context context;
    private ClickPositiveListener clickPositiveListener;

    public FeedbackDialog(@NonNull Context context, boolean isSuccess) {
        super(context, R.style.dialogTheme);
        this.context = context;
        this.isSuccess = isSuccess;
    }

    public void setClickPositiveListener(ClickPositiveListener clickPositiveListener) {
        this.clickPositiveListener = clickPositiveListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback_dialog);
        content = findViewById(R.id.content);
        negativeBtn = findViewById(R.id.negativeBtn);
        positiveBtn = findViewById(R.id.positiveBtn);

        if (isSuccess) {
            content.setText(context.getResources().getString(R.string.feedback_success));
            negativeBtn.setVisibility(View.GONE);
            positiveBtn.setText(context.getResources().getString(R.string.ok));
        } else {
            content.setText(context.getResources().getString(R.string.feedback_failed));
            positiveBtn.setText(context.getResources().getString(R.string.ok));
        }

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (clickPositiveListener != null) {
                    clickPositiveListener.onClick();
                }
            }
        });
    }

    public interface ClickPositiveListener {
        void onClick();
    }
}
