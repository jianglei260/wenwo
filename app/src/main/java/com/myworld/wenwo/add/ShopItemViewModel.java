package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.ObservableField;

import com.myworld.wenwo.add.base.AddItemViewModel;

/**
 * Created by jianglei on 16/7/20.
 */

public class ShopItemViewModel extends AddItemViewModel {
    public ObservableField<String> content = new ObservableField<>();

    @Override
    public String getContentText() {
        return content.get();
    }
    private Context context;

    public ShopItemViewModel(Context context) {
        this.context = context;
    }
}
