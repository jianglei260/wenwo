package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;

import com.myworld.wenwo.add.base.AddItemViewModel;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.view.widget.MapViewWrapper;

/**
 * Created by jianglei on 16/7/20.
 */

public class AddressItemViewModel extends AddItemViewModel {
    private Context context;
    public ObservableField<String> address = new ObservableField<>();
    public ObservableDouble geoX = new ObservableDouble();
    public ObservableDouble geoY = new ObservableDouble();
    public MapViewWrapper.AddressChangedListener listener=new MapViewWrapper.AddressChangedListener() {
        @Override
        public void onChanged() {

        }
    };

    @Override
    public String getContentText() {
        return address.get();
    }

    public AddressItemViewModel(Context context) {
        this.context = context;
    }
}
