package com.example.ming.bluetoothcollect.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.qmuiteam.qmui.widget.QMUIViewPager;

public class NoSlidingViewPager extends QMUIViewPager {

    private boolean slide = false;// false 禁止ViewPager左右滑动。

    public NoSlidingViewPager(Context context) {
        super(context);
    }

    public NoSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean slide) {
        this.slide = slide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return slide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return slide;
    }

}
