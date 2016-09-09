package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/8/8.
 */

public class TitlebarBehavior extends CoordinatorLayout.Behavior implements NestedScrollView.OnScrollChangeListener {
    int backgroundColor;
    int textColor;
    private TitleBar titleBar;

    public TitlebarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundColor = context.getResources().getColor(R.color.bar_background_color);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency.getId() == R.id.scrollView) {
            titleBar = (TitleBar) child;
            textColor = titleBar.getTextView().getCurrentTextColor() & 0xFF;
            child.setBackgroundColor(Color.argb(0, 248, 248, 248));
            titleBar.getTextView().setTextColor(Color.WHITE);
            ((NestedScrollView) dependency).setOnScrollChangeListener(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int scrolledHeight = scrollY;
        int titleBarHeight = titleBar.getHeight();
        if (scrolledHeight > 0) {
            float alpha = scrolledHeight >= titleBarHeight ? 1 : (float) scrolledHeight / titleBarHeight;
            titleBar.setBackgroundColor(Color.argb((int) (alpha * 255), 248, 248, 248));
            int color = (int) ((1 - alpha) * 255);
            titleBar.getTextView().setTextColor(Color.argb(255, color, color, color));
        }

    }
}
