package com.example.c424.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.c424.R;

public class GradientFrame extends View {
    /**
     * 字体内容
     */
    private String mHaoJujuText="豪狙狙";
    /**
     * 字体大小
     */
    private int mHaoJujuTextSize;
    /**
     * 字体颜色
     */
    private int mHaoJujuTextColor;
    /**
     * 渐变开始的颜色
     */
    private int mHaoJujuStartColor;
    /**
     * 渐变结束的颜色
     */
    private int mHaoJujuEndColor;

    /**
     * 渐变结束的颜色
     */
    private int mHaoJujurectRadius;


    public GradientFrame(Context context) {
        this(context, null);
    }

    public GradientFrame(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GradientFrameStyle, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.GradientFrameStyle_frameTextSize:

                    mHaoJujuTextSize = array.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.GradientFrameStyle_frameTextColor:

                    mHaoJujuTextColor = array.getColor(index, Color.BLACK);
                    break;
                case R.styleable.GradientFrameStyle_frameStartColor:
                    mHaoJujuStartColor = array.getColor(index, Color.RED);
                    break;
                case R.styleable.GradientFrameStyle_frameEndColor:
                    mHaoJujuEndColor = array.getColor(index, Color.BLACK);
                    break;

                case R.styleable.GradientFrameStyle_frameRectRadius:
                    mHaoJujurectRadius = array.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()
                    ));
                    break;
                case R.styleable.GradientFrameStyle_frameText:
                    mHaoJujuText = array.getString(R.styleable.GradientFrameStyle_frameText);
                    break;

            }
        }
        array.recycle();

        init();
    }

    public void setText() {

    }

    public GradientFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
    }

    private Paint mPaint;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient gradient = new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, mHaoJujuStartColor, mHaoJujuEndColor, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);
        RectF rect = new RectF(2f, 2f, getWidth()-2, getHeight()-2);
        canvas.drawRoundRect(rect, mHaoJujurectRadius, mHaoJujurectRadius, mPaint);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mHaoJujuTextColor);
        mPaint.setTextSize(mHaoJujuTextSize);

        Rect rectText = new Rect(); // 文字所在区域的矩形
        mPaint.getTextBounds(mHaoJujuText, 0, mHaoJujuText.length(), rectText);
        int textWidth= rectText.width();
        int textHeight = rectText.height();
        canvas.drawText(mHaoJujuText, getWidth() / 2-textWidth/2 , getHeight() / 2 +textHeight/2, mPaint);
    }
}
