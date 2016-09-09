package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/18.
 */

public class ListTimelineView extends TimelineView {
    private TextView descTextView;
    private RecyclerView tagRecyclerView;
    private String listDesc;

    public ListTimelineView(Context context) {
        this(context, null);
    }

    public ListTimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ListTimelineView, 0, 0);
            listDesc = ta.getString(R.styleable.ListTimelineView_listDesc);
            ta.recycle();
        }
        initUI();
    }

    private void initUI() {
        int padding = getResources().getDimensionPixelSize(R.dimen.list_timeline_left_padding);
        container.setPadding(padding, 0, 0, 0);
        LinearLayout content = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_timeline_layou, null, false);
        descTextView = (TextView) content.findViewById(R.id.descText);
        descTextView.setText(listDesc);
        tagRecyclerView = (RecyclerView) content.findViewById(R.id.tag_recycler_view);
        container.addView(content);
    }

    public RecyclerView getTagRecyclerView() {
        return tagRecyclerView;
    }

    public String getListDesc() {
        return listDesc;
    }

    public void setListDesc(String listDesc) {
        this.listDesc = listDesc;
        descTextView.setText(listDesc);
    }
}
