package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/18.
 */

public class SimpleTextTimelineView extends TimelineView {
    TextView contentTextView;
    private String contentText;
    public SimpleTextTimelineView(Context context) {
        this(context,null);
    }

    public SimpleTextTimelineView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleTextTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs!=null){
            TypedArray ta=getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.SimpleTextTimelineView,0,0);
            contentText=ta.getString(R.styleable.SimpleTextTimelineView_contentText);
        }
        initUI();
    }

    private void initUI() {
        contentTextView=new TextView(getContext());
        contentTextView.setText(contentText);
        container.addView(contentTextView);
        int padding=getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        container.setPadding(padding,padding,padding,padding);
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
        contentTextView.setText(contentText);
    }
}
