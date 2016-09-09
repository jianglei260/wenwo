package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jianglei on 16/9/7.
 */

public class ScrollbleViewPager extends ViewPager {
    private boolean scrollble = true;

    public ScrollbleViewPager(Context context) {
        super(context);
    }

    public ScrollbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (scrollble) {
            super.scrollTo(x, y);
        }
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return super.dispatchHoverEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            int size = getChildCount();
            for (int i = 0; i < size; i++) {
                View child = getChildAt(i);
                if (child instanceof ViewGroup) {
                    child.dispatchTouchEvent(ev);
                } else {
                    child.onTouchEvent(ev);
                }
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
