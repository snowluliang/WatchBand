package com.snow.watchband;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by snow on 2016/11/1.
 */

public class Watch extends View {

    private String mTitletext;//文本
    private int mTitleColor;//文本颜色
    private int mTitleSize;//文本大小

    //控制文本回执的范围
    private Rect mBound;
    private Paint mPaint;

    public Watch(Context context) {
        this(context, null);
    }

    public Watch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获得自定义的样式属性
     *
     */
    public Watch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获得自定义样式的属性
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.Watch, defStyleAttr, 0);

        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.Watch_titleText:
                    mTitletext = array.getString(attr);
                    break;
                case R.styleable.Watch_titleTextColor:
                    //默认颜色设置为黑色
                    mTitleColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.Watch_titleTextSize:
                    //默认设置为16sp TypedValue可以将sp转化为px
                    mTitleSize = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitletext = randomText();
                postInvalidate();
            }
        });
        array.recycle();
        /**
         * 获取文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitletext, 0, mTitletext.length(), mBound);
    }

    private String randomText() {
        Random random = new Random();
        List<Integer> mLists = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        while (mLists.size() < 4) {
            int num = random.nextInt(10);
            mLists.add(Math.abs(num));
        }
        for (Integer list : mLists) {
            sb.append(list + "");
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleSize);
            mPaint.getTextBounds(mTitletext, 0, mTitletext.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleSize);
            mPaint.getTextBounds(mTitletext, 0, mTitletext.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;

        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleColor);
        canvas.drawText(mTitletext, getWidth() / 2 - mBound.width() / 2,
                getHeight() / 2 + mBound.height() / 2, mPaint);

    }
}
