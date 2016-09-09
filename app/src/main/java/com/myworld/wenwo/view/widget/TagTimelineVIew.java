package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexboxLayout;
import com.myworld.wenwo.R;

import java.util.List;

/**
 * Created by jianglei on 16/9/6.
 */

public class TagTimelineVIew extends TimelineView {
    public FlexboxLayout flexboxLayout;
    public TextView contentTextView;

    public TagTimelineVIew(Context context) {
        this(context, null);
    }

    public TagTimelineVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagTimelineVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        contentTextView = new TextView(getContext());
        contentTextView.setText(getResources().getString(R.string.write_food_type));
        container.addView(contentTextView);

        flexboxLayout = new FlexboxLayout(getContext());
        flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        container.setPadding(padding, padding, padding, padding);
        container.addView(flexboxLayout);
        contentTextView.setVisibility(GONE);
    }

    @BindingAdapter("tags")
    public static void setTags(TagTimelineVIew tagTimelineVIew, ObservableList<String> tags) {
        if (tags.size() <= 0)
            tagTimelineVIew.contentTextView.setVisibility(VISIBLE);
        else
            tagTimelineVIew.contentTextView.setVisibility(GONE);
        tagTimelineVIew.flexboxLayout.removeAllViews();
        for (String tag : tags) {
            View root = LayoutInflater.from(tagTimelineVIew.getContext()).inflate(R.layout.tag_item_preview, tagTimelineVIew.flexboxLayout, false);
            TextView tagText = (TextView) root.findViewById(R.id.tag_text);
            tagText.setText(tag);
            tagTimelineVIew.flexboxLayout.addView(root);
        }
    }

}
