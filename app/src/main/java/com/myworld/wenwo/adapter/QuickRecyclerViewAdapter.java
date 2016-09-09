package com.myworld.wenwo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemViewArg;

/**
 * Created by jianglei on 16/7/27.
 */

public class QuickRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter {
    public QuickRecyclerViewAdapter(@NonNull ItemViewArg<T> arg) {
        super(arg);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder((ViewHolder) holder, position, payloads);
    }
}
