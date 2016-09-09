package com.myworld.wenwo.kinds;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.data.entity.Tag;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by jianglei on 16/8/29.
 */

public class KindItemViewModel implements ViewModel {
    public ObservableList<KindTagViewModel> items = new ObservableArrayList<>();
    public ItemViewSelector<KindTagViewModel> itemView = new ItemViewSelector<KindTagViewModel>() {
        @Override
        public void select(ItemView itemView, int position, KindTagViewModel item) {
            if (position == 0) {
                itemView.set(BR.viewModel, R.layout.kind_big_tag_layout);
            } else {
                itemView.set(BR.viewModel, R.layout.kind_tag_layout);
            }
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ObservableBoolean lastHaveBg = new ObservableBoolean();
    public ObservableBoolean lastRowHaveBg = new ObservableBoolean();

    public KindItemViewModel(Context context, Tag tag) {
        lastHaveBg.set(true);
        items.add(new KindTagViewModel(context, tag.type));
        for (int i = 0; i < tag.childs.size(); i++) {
            String tagText = tag.childs.get(i);
            KindTagViewModel tagViewModel = new KindTagViewModel(context, tagText);
            items.add(tagViewModel);
        }
        if (tag.childs.size() < 6) {
            for (int i = 0; i < 6 - tag.childs.size(); i++) {
                KindTagViewModel tagViewModel = new KindTagViewModel(context, "");
                items.add(tagViewModel);
            }
        } else if (tag.childs.size() > 6) {
            int num = 4 - (tag.childs.size() - 6) % 4;
            if (num != 4)
                for (int i = 0; i < num; i++) {
                    KindTagViewModel tagViewModel = new KindTagViewModel(context, "");
                    items.add(tagViewModel);
                }
        }

    }
    public void update(){
        int size = items.size();
        for (int i = 0; i < size; i++) {
            KindTagViewModel tagViewModel = items.get(i);
            if (i <= 3) {
                tagViewModel.isFirstRow.set(true);
                tagViewModel.haveBg.set(!lastHaveBg.get());
            }
            if (i < 7) {
                if (i == 3||i==6)
                    tagViewModel.isLastCol.set(true);
                if (i > 3)
                    tagViewModel.haveBg.set(lastHaveBg.get());
            } else {
                if ((i - 6) % 4 == 0)
                    tagViewModel.isLastCol.set(true);
                else if ((i - 6) % 4 == 1)
                    tagViewModel.isFirstCol.set(true);
                if (((i - 7) / 4) % 2 == 0) {
                    tagViewModel.haveBg.set(!lastHaveBg.get());
                } else {
                    tagViewModel.haveBg.set(lastHaveBg.get());
                }
            }
            if (size <= 7) {
                if (i >= 4) {
                    tagViewModel.isLastRow.set(true);
                }
            } else {
                if (i >= size - 4) {
                    tagViewModel.isLastRow.set(true);
                    lastRowHaveBg.set(tagViewModel.haveBg.get());
                }
            }
            tagViewModel.updateCorner();
        }
    }
}
