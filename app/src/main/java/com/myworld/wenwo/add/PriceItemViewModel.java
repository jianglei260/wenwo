package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;

import com.myworld.wenwo.add.base.AddItemViewModel;

/**
 * Created by jianglei on 16/7/20.
 */

public class PriceItemViewModel extends AddItemViewModel {
    public ObservableField<String> content = new ObservableField<>();
    public ObservableFloat maxValue = new ObservableFloat(5.0f);
    private Context context;
    @Override
    public String getContentText() {
        return content.get();
    }

    public PriceItemViewModel(Context context) {
        this.context = context;
    }
}
