package com.myworld.wenwo.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/18.
 */

public class TimelineView extends RelativeLayout {
    protected View line;
    protected TextView title;
    protected SimpleDraweeView image;
    protected FrameLayout container;

    private String titleText;
    private int lineColor;
    private int imageRes;
    private boolean lineVisible;
    private boolean isFirst = true;

    public TimelineView(Context context) {
        this(context, null, 0);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TimelineView, 0, 0);
            titleText = ta.getString(R.styleable.TimelineView_lineTitle);
            lineColor = ta.getColor(R.styleable.TimelineView_lineColor, Color.LTGRAY);
            imageRes = ta.getResourceId(R.styleable.TimelineView_lineImage, R.mipmap.ic_launcher);
            lineVisible = ta.getBoolean(R.styleable.TimelineView_lineVisible, false);
            ta.recycle();
        }
        init();

    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.timeline_layout, this, true);
        line = parent.findViewById(R.id.line);
        title = (TextView) parent.findViewById(R.id.title);
        image = (SimpleDraweeView) parent.findViewById(R.id.image);
        container = (FrameLayout) parent.findViewById(R.id.container);
        SpannableString text = new SpannableString(titleText);
        if (titleText.contains("(")) {
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), titleText.indexOf("("), titleText.indexOf(")") + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        title.setText(text);
        image.setImageResource(imageRes);

        if (!lineVisible)
            line.setVisibility(GONE);
        else {
            line.setVisibility(VISIBLE);
            line.setBackgroundColor(lineColor);
        }
        container.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (lineVisible) {
                    int width = getResources().getDimensionPixelSize(R.dimen.line_width);
                    LayoutParams layoutParams = new LayoutParams(width, bottom - top);
                    layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.time_line_left_margin);
                    layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.time_line_top_margin);
                    line.setLayoutParams(layoutParams);
                    invalidate();
                }
            }
        });
    }
}
