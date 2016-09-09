package com.myworld.wenwo.address;

import android.content.Context;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.found.FoundFragment;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/9/1.
 */

public class AddressItemViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> addressText = new ObservableField<>();
    public ObservableDouble geoX=new ObservableDouble();
    public ObservableDouble geoY=new ObservableDouble();
    public ObservableInt hot=new ObservableInt();
    public ReplyCommand itemClick=new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Config.geo_latitude=geoX.get();
            Config.geo_longtitude=geoY.get();
            Config.geo_address.set(addressText.get());
            EventBus.getInstance().onEevent(FoundFragment.class,"reload");
            ((AddressSearchActivity)context).finish();
        }
    });

    public AddressItemViewModel(Context context) {
        this.context = context;
    }
}
