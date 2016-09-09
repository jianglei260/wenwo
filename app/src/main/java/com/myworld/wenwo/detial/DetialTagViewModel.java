package com.myworld.wenwo.detial;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.add.DetailItemViewModel;
import com.myworld.wenwo.data.entity.DetialTag;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/18.
 */

public class DetialTagViewModel implements ViewModel {
    private Context context;
    private DetialTag tag;
    public ObservableField<String> tagName = new ObservableField<>();
    public ObservableField<String> tagValue = new ObservableField<>();
    public ObservableBoolean editable = new ObservableBoolean(false);
    public ObservableBoolean visible = new ObservableBoolean(false);
    public ReplyCommand removeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            detailItemViewModel.removeItem(DetialTagViewModel.this);
        }
    });
    private DetailItemViewModel detailItemViewModel;
    public ReplyCommand addClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            detailItemViewModel.addItem();
        }
    });

    public DetialTagViewModel(Context context, DetialTag tag) {
        this.context = context;
        this.tag = tag;
        tagName.set(tag.getName());
        tagValue.set(tag.getVal());
    }

    public void setDetailItemViewModel(DetailItemViewModel detailItemViewModel) {
        this.detailItemViewModel = detailItemViewModel;
    }
}
