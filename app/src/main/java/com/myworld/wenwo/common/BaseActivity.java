package com.myworld.wenwo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.utils.ActivityTool;
import com.myworld.wenwo.view.widget.CustomDraweeView;

/**
 * Created by jianglei on 16/8/14.
 */

public class BaseActivity<T> extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (name.equals("com.facebook.drawee.view.SimpleDraweeView"))
                    return new CustomDraweeView(context, attrs);
                else if (name.equals("android.support.v4.widget.SwipeRefreshLayout")){
                    SwipeRefreshLayout swipeRefreshLayout=new SwipeRefreshLayout(context, attrs);
                    swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
                    return swipeRefreshLayout;
                }
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                return view;

            }
        });
        ActivityTool.onActivityCreated(this);
        super.onCreate(savedInstanceState);
    }

    public void onObjectSet(T t) {
    }
}
