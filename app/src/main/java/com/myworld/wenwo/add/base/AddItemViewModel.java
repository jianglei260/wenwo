package com.myworld.wenwo.add.base;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by jianglei on 16/7/20.
 */

public abstract class AddItemViewModel implements ViewModel{
    public abstract String getContentText();

    public static String TAG;
}
