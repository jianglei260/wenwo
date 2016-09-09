package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/6/6.
 */

public class TitleBar extends RelativeLayout {
    private ImageView leftImage, rightImage;
    private TextView textView;
    @DrawableRes
    private int leftImageRes, rightImageRes;
    private boolean leftImageVisible = true, rightImageVisible;
    private String titleText;
    private GestureDetector gestureDetector;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        titleText = ta.getString(R.styleable.TitleBar_titleText);
        leftImageRes = ta.getResourceId(R.styleable.TitleBar_leftImage, R.mipmap.logo);
        rightImageRes = ta.getResourceId(R.styleable.TitleBar_rightImage, R.mipmap.logo);
        leftImageVisible = ta.getBoolean(R.styleable.TitleBar_leftImageVisible, true);
        rightImageVisible = ta.getBoolean(R.styleable.TitleBar_rightImageVisible, false);
        ta.recycle();
        initView();
    }

    private void initView() {
        int bgColor = getResources().getColor(R.color.bar_background_color);
        setBackgroundColor(bgColor);
        leftImage = new ImageView(getContext());
        rightImage = new ImageView(getContext());
        textView = new TextView(getContext());

        int titleBarHeight = getResources().getDimensionPixelSize(R.dimen.title_bar_height);
        int titleBarPadding = getResources().getDimensionPixelSize(R.dimen.title_bar_image_padding);
        LayoutParams leftImageParams = new LayoutParams(titleBarHeight, titleBarHeight);
        leftImage.setVisibility(leftImageVisible ? VISIBLE : INVISIBLE);
        leftImage.setPadding(titleBarPadding, titleBarPadding, titleBarPadding, titleBarPadding);
        addView(leftImage, leftImageParams);

        LayoutParams rightImageParams = new LayoutParams(titleBarHeight, titleBarHeight);
        rightImage.setVisibility(rightImageVisible ? VISIBLE : INVISIBLE);
        rightImageParams.addRule(ALIGN_PARENT_RIGHT);
        rightImage.setPadding(titleBarPadding, titleBarPadding, titleBarPadding, titleBarPadding);
        addView(rightImage, rightImageParams);
        int width = getResources().getDimensionPixelSize(R.dimen.title_bar_text_width);
        int height = getResources().getDimensionPixelSize(R.dimen.title_bar_text_height);
        LayoutParams titleTextParams = new LayoutParams(width, height);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.GRAY);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        titleTextParams.addRule(CENTER_VERTICAL);
        titleTextParams.addRule(CENTER_HORIZONTAL);
        addView(textView, titleTextParams);

        if (!TextUtils.isEmpty(titleText))
            textView.setText(titleText);
        if (leftImageRes != 0)
            leftImage.setImageResource(leftImageRes);
        if (rightImageRes != 0)
            rightImage.setImageResource(rightImageRes);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null)
            return gestureDetector.onTouchEvent(event);
        else
            return super.onTouchEvent(event);
    }

    public void addDoubleClickListener(GestureDetector.OnDoubleTapListener listener) {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        gestureDetector.setOnDoubleTapListener(listener);
    }

    public ImageView getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(ImageView leftImage) {
        this.leftImage = leftImage;
    }

    public ImageView getRightImage() {
        return rightImage;
    }

    public void setRightImage(ImageView rightImage) {
        this.rightImage = rightImage;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getLeftImageRes() {
        return leftImageRes;
    }

    public void setLeftImageRes(int leftImageRes) {
        this.leftImageRes = leftImageRes;
    }

    public int getRightImageRes() {
        return rightImageRes;
    }

    public void setRightImageRes(int rightImageRes) {
        this.rightImageRes = rightImageRes;
    }

    public boolean isLeftImageVisible() {
        return leftImageVisible;
    }

    public void setLeftImageVisible(boolean leftImageVisible) {
        this.leftImageVisible = leftImageVisible;
    }

    public boolean isRightImageVisible() {
        return rightImageVisible;
    }

    public void setRightImageVisible(boolean rightImageVisible) {
        this.rightImageVisible = rightImageVisible;
        rightImage.setVisibility(rightImageVisible ? VISIBLE : INVISIBLE);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        textView.setText(titleText);
    }

    interface DoubbleClickListener {
        public void onDoubbleClick();
    }
}
