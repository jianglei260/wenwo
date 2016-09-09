package com.myworld.wenwo.topics;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.data.entity.BannerItem;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.utils.ObservableUtil;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/30.
 */

public class TopicViewModel implements ViewModel {
    private Context context;
    public ObservableBoolean isRefresh = new ObservableBoolean(true);
    public ObservableList<TopicItemViewModel> items = new ObservableArrayList<>();
    public ItemViewSelector<TopicItemViewModel> itemView = new ItemViewSelector<TopicItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, TopicItemViewModel item) {
            itemView.set(BR.itemViewModel, R.layout.topic_item_layout);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ReplyCommand refreshCommand = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!isRefresh.get())
                findTopics();
        }
    });

    public TopicViewModel(final Context context) {
        this.context = context;
        findTopics();
    }

    public void findTopics() {
        isRefresh.set(true);
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<BannerItem>>() {
            @Override
            public void call(Subscriber<? super List<BannerItem>> subscriber) {
                subscriber.onNext(AskMeRepository.getInstance().getBanner());
            }
        }, new Subscriber<List<BannerItem>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<BannerItem> bannerItems) {
                isRefresh.set(false);
                items.clear();
                for (BannerItem item : bannerItems) {
                    items.add(new TopicItemViewModel(context, item));
                }
            }
        });
    }
}
