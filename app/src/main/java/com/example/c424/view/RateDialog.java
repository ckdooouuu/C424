package com.example.c424.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.c424.R;

public class RateDialog extends Dialog {
    private Context context;
    private ImageView firstStar;
    private ImageView secondStar;
    private ImageView thirdStar;
    private ImageView fourthStar;
    private ImageView fifthStar;

    private ImageView[] startArray = new ImageView[5];

    public RateDialog(@NonNull Context context) {
        super(context, R.style.dialogTheme);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.layout_rate_dialog);

        firstStar = findViewById(R.id.firstStar);
        secondStar = findViewById(R.id.secondStar);
        thirdStar = findViewById(R.id.thirdStar);
        fourthStar = findViewById(R.id.fourthStar);
        fifthStar = findViewById(R.id.fifthStar);

        startArray[0] = firstStar;
        startArray[1] = secondStar;
        startArray[2] = thirdStar;
        startArray[3] = fourthStar;
        startArray[4] = fifthStar;

        findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        firstStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedStart(1);
            }
        });
        secondStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedStart(2);
            }
        });
        thirdStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedStart(3);
            }
        });
        fourthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedStart(4);
            }
        });
        fifthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedStart(5);
            }
        });
    }

    private void setSelectedStart(int starNum) {
        for (int i = 0; i < starNum; i++) {
            startArray[i].setImageResource(R.mipmap.ic_rate_check);
        }
        firstStar.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                if (starNum >= 4) {
                    String pkgName = context.getPackageName();
                    String url = "http://play.google.com/store/apps/details?id=" + pkgName;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }
        }, 200);
    }
}
