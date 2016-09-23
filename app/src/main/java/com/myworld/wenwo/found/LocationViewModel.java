package com.myworld.wenwo.found;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.address.AddressSearchActivity;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.common.viewmodel.ListItemViewModel;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/9/1.
 */

public class LocationViewModel extends ListItemViewModel {
    private Context context;
    public ObservableField<String> locationText = new ObservableField<>();
    public ObservableField<String> dsiatanceSelected = new ObservableField<>();
    public ObservableField<String> dsiatance0 = new ObservableField<>();
    public ObservableField<String> dsiatance1 = new ObservableField<>();
    public ObservableField<String> dsiatance2 = new ObservableField<>();
    public int[] ranges = new int[]{1000, 2000, 10000, Integer.MAX_VALUE};
    public static final String DISTANCE1KM = "1km";
    public static final String DISTANCE2KM = "2km";
    public static final String DISTANCE10KM = "10km";
    public static final String DISTANCEALL = "所有信息";
    private AskViewModel askViewModel;
    public ReplyCommand dsiatanceSelectedClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!visible.get()) {
                visible.set(true);
                updateDistance(dsiatanceSelected.get());
            } else {
                visible.set(false);
            }

        }
    });
    public ReplyCommand dsiatance0Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            visible.set(false);
            updateDistance(dsiatance0.get());
            askViewModel.refresh();
        }
    });
    public ReplyCommand dsiatance2Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            visible.set(false);
            updateDistance(dsiatance2.get());
            askViewModel.refresh();
        }
    });
    public ReplyCommand dsiatance1Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            visible.set(false);
            updateDistance(dsiatance1.get());
            askViewModel.refresh();
        }
    });
    public ReplyCommand locationClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, AddressSearchActivity.class);
            context.startActivity(intent);
        }
    });
    public ObservableBoolean visible = new ObservableBoolean(false);

    public void updateDistance(String text) {
        switch (text) {
            case DISTANCE1KM:
                dsiatance0.set(DISTANCE2KM);
                dsiatance1.set(DISTANCE10KM);
                dsiatance2.set(DISTANCEALL);
                dsiatanceSelected.set(DISTANCE1KM);
                Config.range = ranges[0];
                break;
            case DISTANCE2KM:
                dsiatance0.set(DISTANCE1KM);
                dsiatance1.set(DISTANCE10KM);
                dsiatance2.set(DISTANCEALL);
                dsiatanceSelected.set(DISTANCE2KM);
                Config.range = ranges[1];
                break;
            case DISTANCE10KM:
                dsiatance0.set(DISTANCE1KM);
                dsiatance1.set(DISTANCE2KM);
                dsiatance2.set(DISTANCEALL);
                dsiatanceSelected.set(DISTANCE10KM);
                Config.range = ranges[2];
                break;
            case DISTANCEALL:
                dsiatance0.set(DISTANCE1KM);
                dsiatance1.set(DISTANCE2KM);
                dsiatance2.set(DISTANCE10KM);
                dsiatanceSelected.set(DISTANCEALL);
                Config.range = ranges[3];
                break;
        }
    }

    public LocationViewModel(Context context, AskViewModel askViewModel) {
        this.context = context;
        this.askViewModel = askViewModel;
        updateDistance(DISTANCE2KM);
        Config.geo_address.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                locationText.set(Config.geo_address.get());
            }
        });
        locationText.set(Config.geo_address.get());
    }
}
