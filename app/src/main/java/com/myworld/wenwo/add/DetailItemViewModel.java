package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.base.AddItemViewModel;
import com.myworld.wenwo.data.entity.DetialTag;
import com.myworld.wenwo.detial.DetialTagViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 16/7/20.
 */

public class DetailItemViewModel extends AddItemViewModel {
    public ObservableField<String> content = new ObservableField<>();
    public ObservableList<DetialTagViewModel> tags = new ObservableArrayList<>();
    private Context context;
    public final ItemViewSelector<DetialTagViewModel> itemView = new ItemViewSelector<DetialTagViewModel>() {
        @Override
        public void select(ItemView itemView, int position, DetialTagViewModel item) {
            if (position == tags.size() - 1) {
                itemView.setLayoutRes(R.layout.detail_tag_add_item);
                itemView.setBindingVariable(BR.itemViewModel);
            } else {
                itemView.setLayoutRes(R.layout.detial_tag_item);
                itemView.setBindingVariable(BR.itemViewModel);
            }

        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public DetailItemViewModel(Context context) {
        this.context = context;
        DetialTagViewModel detialTagViewModel = new DetialTagViewModel(context, new DetialTag());
        detialTagViewModel.setDetailItemViewModel(this);

        tags.add(detialTagViewModel);
    }

    public void addItem() {
        DetialTagViewModel detialTagViewModel = new DetialTagViewModel(context, new DetialTag());
        detialTagViewModel.setDetailItemViewModel(this);
        detialTagViewModel.editable.set(true);
        detialTagViewModel.visible.set(true);
        tags.add(tags.size() - 1, detialTagViewModel);
    }

    public void removeItem(DetialTagViewModel detialTagViewModel) {
        tags.remove(detialTagViewModel);
    }

    @Override
    public String getContentText() {
        return content.get();
    }
}
