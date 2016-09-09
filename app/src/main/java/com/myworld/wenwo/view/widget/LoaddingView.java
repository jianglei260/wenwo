package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/26.
 */

public class LoaddingView extends LinearLayout {
    private AnimationDrawable animationDrawable;
    private SimpleDraweeView imageView;
    private String titleText;
    private boolean horizontal = true;
    private TextView loadText;

    public LoaddingView(Context context) {
        this(context, null);
    }

    public LoaddingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaddingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.LoaddingView, 0, 0);
            titleText = ta.getString(R.styleable.LoaddingView_lodding_text);
            horizontal = ta.getBoolean(R.styleable.LoaddingView_horizontal, true);
            ta.recycle();
        }
        initUI();
    }

    private void initUI() {
        Log.d("new loaddingview", "init");
        imageView = new SimpleDraweeView(getContext());
        imageView.setImageResource(R.drawable.animation_list_loadiing);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        addView(imageView, params);
        loadText = new TextView(getContext());
        loadText.setTextColor(getResources().getColor(R.color.colorPrimary));
        if (TextUtils.isEmpty(titleText))
            loadText.setText(getResources().getString(R.string.default_load_text));
        else
            loadText.setText(titleText);
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        if (!horizontal)
            setOrientation(VERTICAL);
        addView(loadText, textParams);
        animationDrawable.start();
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        loadText.setText(titleText);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
//        if (changedView.equals(this)) {
//            if (visibility == INVISIBLE||visibility==GONE) {
//                if (animationDrawable!=null&&animationDrawable.isRunning())
//                animationDrawable.stop();
//            }
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animationDrawable!=null&&animationDrawable.isRunning())
            animationDrawable.stop();
    }
}
