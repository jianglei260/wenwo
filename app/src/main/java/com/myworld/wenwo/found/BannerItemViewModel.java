package com.myworld.wenwo.found;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.myworld.wenwo.common.viewmodel.ListItemViewModel;
import com.myworld.wenwo.data.entity.BannerItem;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.web.WebActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by jianglei on 16/8/10.
 */

public class BannerItemViewModel extends ListItemViewModel {
    private Context context;
    public ObservableList<BannerItem> items = new ObservableArrayList<>();
    public OnBannerClickListener bannerClickListener = new OnBannerClickListener() {
        @Override
        public void OnBannerClick(int position) {
            WebActivity.startWebActivity(context, items.get(position - 1).getCarouselClickURL() + "?type=app", items.get(position - 1).getCarouselName());
        }
    };

    public BannerItemViewModel(Context context) {
        this.context = context;
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<BannerItem>>() {
            @Override
            public void call(Subscriber<? super List<BannerItem>> subscriber) {
                subscriber.onNext(AskMeRepository.getInstance().getBannerFromCache());
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
                items.clear();
                items.addAll(bannerItems);
                updateBanner();
            }
        });
    }

    public void updateBanner() {
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
                items.clear();
                items.addAll(bannerItems);
            }
        });
    }

    @Override
    public void dataChanged() {

    }
}
