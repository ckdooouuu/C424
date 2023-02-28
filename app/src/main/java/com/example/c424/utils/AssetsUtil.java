package com.example.c424.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class AssetsUtil {
    public static Bitmap getFlag(Context context, String flagName) {
        flagName = "flag/" + flagName + ".png";
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(flagName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getFlag Exception: " + e.getMessage());
        }
        return image;
    }
}
