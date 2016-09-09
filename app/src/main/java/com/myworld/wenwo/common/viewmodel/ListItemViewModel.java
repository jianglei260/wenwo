package com.myworld.wenwo.common.viewmodel;

import android.databinding.ObservableInt;

/**
 * Created by jianglei on 16/7/27.
 */

public class ListItemViewModel {
    public ObservableInt viewType = new ObservableInt();
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_LOAD_MORE = 1;
    public static final int VIEW_TYPE_LOAD_FINISH = 2;
    public static final int VIEW_TYPE_BANNER = 3;
    public static final int VIEW_TYPE_NAVIGATION = 4;
    public static final int VIEW_TYPE_LOCATION = 5;

    public void dataChanged() {
    }

}
