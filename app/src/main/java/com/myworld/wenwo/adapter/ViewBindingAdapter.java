package com.myworld.wenwo.adapter;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by jianglei on 16/8/31.
 */

public class ViewBindingAdapter {
    @BindingAdapter({"image_uri"})
    public static void setImageUri(final SimpleDraweeView simpleDraweeView, final String uri) {
        if (simpleDraweeView.getContext() instanceof Activity) {
            ((Activity) simpleDraweeView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(uri)) {
                        simpleDraweeView.setImageURI(Uri.parse(uri));
                    }
                }
            });
        }
    }
}
