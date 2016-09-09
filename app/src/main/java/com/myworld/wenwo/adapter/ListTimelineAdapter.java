package com.myworld.wenwo.adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.widget.RecyclerView;

import com.myworld.wenwo.R;
import com.myworld.wenwo.view.widget.ListTimelineView;

import java.util.List;

import me.tatarka.bindingcollectionadapter.BindingCollectionAdapter;
import me.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.LayoutManagers;
import me.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;

/**
 * Created by jianglei on 16/7/18.
 */

public class ListTimelineAdapter {
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemView", "items", "adapter", "itemIds"}, requireAll = false)
    public static <T> void setAdapter(ListTimelineView timelineView, ItemViewArg<T> arg, List<T> items, BindingRecyclerViewAdapterFactory factory, BindingRecyclerViewAdapter.ItemIds<T> itemIds) {
        RecyclerView recyclerView=timelineView.getTagRecyclerView();
        if (arg == null) {
            throw new IllegalArgumentException("itemView must not be null");
        }
        if (factory == null) {
            factory = BindingRecyclerViewAdapterFactory.DEFAULT;
        }
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(recyclerView, arg);
            adapter.setItemIds(itemIds);
            recyclerView.setAdapter(adapter);
        }
        int itemHeight=recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.tag_item_height);
        recyclerView.setMinimumHeight(itemHeight*items.size());
        adapter.setItems(items);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(ListTimelineView timelineView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        RecyclerView recyclerView=timelineView.getTagRecyclerView();
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingConversion
    public static BindingRecyclerViewAdapterFactory toRecyclerViewAdapterFactory(final String className) {
        return new BindingRecyclerViewAdapterFactory() {
            @Override
            public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
                return createClass(className, arg);
            }
        };
    }
    @SuppressWarnings("unchecked")
    static <T, A extends BindingCollectionAdapter<T>> A createClass(String className, ItemViewArg<T> arg) {
        try {
            return (A) Class.forName(className).getConstructor(ItemViewArg.class).newInstance(arg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
