package com.myworld.wenwo.vipcard;

import android.content.Context;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.myworld.wenwo.data.entity.AskMe;

/**
 * Created by jianglei on 16/9/14.
 */

public class VipCardViewModel implements ViewModel {
    private Context context;
    private AskMe askMe;
    public ObservableField<String> cardContent = new ObservableField<>();
    public ObservableField<String> cardImage = new ObservableField<>();

    public VipCardViewModel(Context context, AskMe askMe) {
        this.context = context;
        this.askMe = askMe;
        cardContent.set(askMe.getVipCardContent());
        cardImage.set(askMe.getVipCardImage());
    }
}
