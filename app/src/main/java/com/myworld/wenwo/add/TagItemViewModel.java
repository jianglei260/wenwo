package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;

import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.base.AddItemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 16/7/20.
 */

public class TagItemViewModel extends AddItemViewModel {
    private Context context;
    public ObservableList<TagViewModel> tags = new ObservableArrayList<>();
    public ObservableList<String> content = new ObservableArrayList<>();
    public ObservableField<String> contentString = new ObservableField<>();
    public ItemViewSelector<TagViewModel> itemView = new ItemViewSelector<TagViewModel>() {
        @Override
        public void select(ItemView itemView, int position, TagViewModel item) {
            itemView.set(BR.tagViewModel, R.layout.tag_item_view);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public void contentChanged(String value) {

        String[] contentTags = value.split(";");
        if (contentTags.length > 3) {
            Toast.makeText(context, context.getString(R.string.max_tag_notice), Toast.LENGTH_SHORT).show();
            return;
        }
        content.clear();
        for (String contentTag : contentTags) {
            if (contentTag.contains("；")) {
                String[] tags = contentTag.split("；");
                for (String tag : tags) {
                    if (!TextUtils.isEmpty(tag))
                        content.add(tag);
                }
            } else {
                if (!TextUtils.isEmpty(contentTag))
                    content.add(contentTag);
            }
        }
    }

    public TagItemViewModel(Context context) {
        this.context = context;
        tags.add(new TagViewModel("海鲜", this));
        tags.add(new TagViewModel("川菜", this));
        tags.add(new TagViewModel("西餐", this));
        tags.add(new TagViewModel("小吃快餐", this));
        tags.add(new TagViewModel("甜点饮品", this));
        tags.add(new TagViewModel("火锅", this));
        tags.add(new TagViewModel("串串香", this));
        tags.add(new TagViewModel("烧烤烤肉", this));
        tags.add(new TagViewModel("自助餐", this));
        tags.add(new TagViewModel("香锅烤鱼", this));
        contentString.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                contentChanged(contentString.get());
            }
        });
    }

    @Override
    public String getContentText() {
        return contentString.get();
    }
}
