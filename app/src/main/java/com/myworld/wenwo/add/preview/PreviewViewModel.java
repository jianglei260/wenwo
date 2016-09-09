package com.myworld.wenwo.add.preview;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.TagViewModel;
import com.myworld.wenwo.add.edit.EditFragment;
import com.myworld.wenwo.detial.DetialTagViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/21.
 */

public class PreviewViewModel implements ViewModel {
    public ObservableField<String> imageUri = new ObservableField<>();
    public ObservableField<String> shop = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> detial = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableList<DetialTagViewModel> tags = new ObservableArrayList<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> desc = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<String>();
    public ObservableList<String> title = new ObservableArrayList<>();
    private PreviewFragment fragment;
    public final ItemViewSelector<DetialTagViewModel> itemView = new ItemViewSelector<DetialTagViewModel>() {
        @Override
        public void select(ItemView itemView, int position, DetialTagViewModel item) {
            itemView.setLayoutRes(R.layout.detial_tag_item);
            itemView.setBindingVariable(BR.itemViewModel);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ReplyCommand tagClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(0);
        }
    });

    public ReplyCommand reasonClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(1);
        }
    });

    public ReplyCommand detailClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(2);
        }
    });

    public ReplyCommand shopClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(3);
        }
    });

    public ReplyCommand addressClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(4);
        }
    });

    public ReplyCommand priceClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            fragment.showPage(5);
        }
    });


    public PreviewViewModel(PreviewFragment fragment) {
        this.fragment = fragment;
    }
}
