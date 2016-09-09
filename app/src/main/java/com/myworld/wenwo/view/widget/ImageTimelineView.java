package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/18.
 */

public class ImageTimelineView extends TimelineView implements View.OnClickListener {
    private String lineUri;
    private SimpleDraweeView imageView;

    public ImageTimelineView(Context context) {
        this(context, null);
    }

    public ImageTimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTimelineView, 0, 0);
            lineUri = ta.getNonResourceString(R.styleable.ImageTimelineView_lineUri);
            ta.recycle();
        }
        initUI();
    }

    private void initUI() {
        LinearLayout root = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.image_timeline_layout, null);
        imageView = (SimpleDraweeView) root.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_image_default);
        if (!TextUtils.isEmpty(lineUri))
            imageView.setImageURI(Uri.parse(lineUri));
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        container.setPadding(padding, padding, padding, padding);
        container.addView(root);
    }

    public String getLineUri() {
        return lineUri;
    }

    public void setLineUri(String uri) {
        this.lineUri = uri;
        if (!TextUtils.isEmpty(uri))
            imageView.setImageURI(Uri.parse(uri));
    }

    public void setImage(byte[] data) {
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    public void setImage() {

    }

    @Override
    public void onClick(View v) {
    }
}
