package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/8/5.
 */

public class ScrollImageView extends RelativeLayout {
    public ScrollImageView(Context context) {
        this(context, null);
    }

    public ScrollImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_login_bg_half);
        int width = getResources().getDisplayMetrics().widthPixels * 2;
        LayoutParams layoutParams = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(imageView, layoutParams);
    }
}
