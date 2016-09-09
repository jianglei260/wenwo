package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

/**
 * Created by jianglei on 16/8/14.
 */

public class CustomDraweeView extends SimpleDraweeView {
    public CustomDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public CustomDraweeView(Context context) {
        super(context);
    }

    public CustomDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final String TAG = "CustomDraweeView";
    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable drawable = (BitmapDrawable) getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            if (bitmap.isRecycled()) {
                Log.d(TAG, "onDraw: reset Image uri");
                setImageURI(uri);
                onAttach();
                return;
            }
        }
        super.onDraw(canvas);
    }

    protected Uri uri;

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.uri = uri;
    }

    public void setImageURI(@Nullable String uriString, @Nullable Object callerContext) {
        super.setImageURI(uriString, callerContext);
        this.uri = Uri.parse(uriString);
    }
}
