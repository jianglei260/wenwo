package com.myworld.wenwo.found;

import android.databinding.ObservableBoolean;

import com.kelin.mvvmlight.base.ViewModel;

import rx.Observable;

/**
 * Created by jianglei on 16/7/27.
 */

public class LoadMoreViewModel implements ViewModel {
    public ObservableBoolean loadMore = new ObservableBoolean(true);
}
