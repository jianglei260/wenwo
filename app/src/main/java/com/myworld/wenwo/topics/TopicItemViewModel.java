package com.myworld.wenwo.topics;

import android.content.Context;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.data.entity.BannerItem;
import com.myworld.wenwo.web.WebActivity;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/30.
 */

public class TopicItemViewModel implements ViewModel {
    private Context context;
    private BannerItem item;
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ReplyCommand itemClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            WebActivity.startWebActivity(context, item.getCarouselClickURL() + "?type=app", title.get());
        }
    });

    public TopicItemViewModel(Context context, BannerItem item) {
        this.context = context;
        this.item = item;
        imageUrl.set(item.getCarouselImage());
        title.set(item.getCarouselName());
    }
}
