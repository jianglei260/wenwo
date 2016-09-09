package com.myworld.wenwo.application;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.reflect.Method;

/**
 * Created by jianglei on 16/7/25.
 */

public class WenWoResource extends Resources {
    private Context context;

    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     */
    public WenWoResource(Context context, AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
        this.context = context;
    }

    @Override
    public Drawable getDrawable(int id) throws NotFoundException {
        return AppCompatDrawableManager.get().getDrawable(context.getApplicationContext(), id);
    }

}
