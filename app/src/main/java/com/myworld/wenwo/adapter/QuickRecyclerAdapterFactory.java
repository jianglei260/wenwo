package com.myworld.wenwo.adapter;

import android.support.v7.widget.RecyclerView;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;

/**
 * Created by jianglei on 16/7/27.
 */

public class QuickRecyclerAdapterFactory implements BindingRecyclerViewAdapterFactory {
    @Override
    public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
        return new QuickRecyclerViewAdapter<T>(arg);
    }
}
