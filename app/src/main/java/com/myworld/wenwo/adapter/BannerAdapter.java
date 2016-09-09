package com.myworld.wenwo.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myworld.wenwo.data.entity.BannerItem;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnLoadImageListener;

import java.util.List;

/**
 * Created by jianglei on 16/8/10.
 */

public class BannerAdapter {
    @BindingAdapter(value = {"urls", "clickListener"}, requireAll = false)
    public static void setEditable(Banner banner, List<BannerItem> urls, OnBannerClickListener listener) {
        banner.setImages(urls, new OnLoadImageListener() {
            @Override
            public void OnLoadImage(ImageView view, Object url) {
                BannerItem item = (BannerItem) url;
                Glide.with(view.getContext()).load(item.getCarouselImage()).centerCrop().crossFade().into(view);
            }
        });
        banner.setOnBannerClickListener(listener);
        banner.setDelayTime(5000);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
    }

}
