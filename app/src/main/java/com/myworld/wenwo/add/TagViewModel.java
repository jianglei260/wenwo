package com.myworld.wenwo.add;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/20.
 */

public class TagViewModel implements ViewModel {
    public ObservableField<String> tagValue = new ObservableField<>();
    private ObservableField<String> contentText;
    private TagItemViewModel tagItemViewModel;

    public ReplyCommand<TagViewModel> itemClick = new ReplyCommand<TagViewModel>(new Action0() {
        @Override
        public void call() {
            String content = tagItemViewModel.contentString.get();
            tagItemViewModel.contentString.set(TextUtils.isEmpty(content) ? tagValue.get() + ";" : content + tagValue.get()+ ";");
        }
    });

    public TagViewModel setContentText(ObservableField<String> contentText) {
        this.contentText = contentText;
        return this;
    }

    public TagViewModel(String tagValue, TagItemViewModel tagItemViewModel) {
        this.tagValue.set(tagValue);
        this.tagItemViewModel = tagItemViewModel;
    }
}
