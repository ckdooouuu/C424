package com.example.c424.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.c424.R;

public class ReportRateLayout extends LinearLayout implements View.OnClickListener {
    ImageView firstStar;
    ImageView secondStar;
    ImageView thirdStar;
    ImageView fourthStar;
    ImageView fifthStar;
    ImageView[] starArray;
    LinearLayout rateLayout;
    Context context;

    public ReportRateLayout(Context context) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_report_rate, this, false);

        firstStar = view.findViewById(R.id.firstStar);
        secondStar = view.findViewById(R.id.secondStar);
        thirdStar = view.findViewById(R.id.thirdStar);
        fourthStar = view.findViewById(R.id.fourthStar);
        fifthStar = view.findViewById(R.id.fifthStar);
        rateLayout = view.findViewById(R.id.rateLayout);

        starArray = new ImageView[]{firstStar, secondStar, thirdStar, fourthStar, fifthStar};

        firstStar.setOnClickListener(this);
        secondStar.setOnClickListener(this);
        thirdStar.setOnClickListener(this);
        fourthStar.setOnClickListener(this);
        fifthStar.setOnClickListener(this);

        addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstStar:
                updateStarView(0);
                break;
            case R.id.secondStar:
                updateStarView(1);
                break;
            case R.id.thirdStar:
                updateStarView(2);
                break;
            case R.id.fourthStar:
                updateStarView(3);
                break;
            case R.id.fifthStar:
                updateStarView(4);
                break;
        }
    }

    private void updateStarView(int index) {
        for (int i = 0; i <= index; i++) {
            starArray[i].setImageResource(R.mipmap.ic_rate_check);
        }
        if (index <= 3) {
            rateLayout.setVisibility(GONE);
        } else {
            String pkgName = context.getPackageName();
            String url = "http://play.google.com/store/apps/details?id=" + pkgName;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
