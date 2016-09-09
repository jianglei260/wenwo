package com.myworld.wenwo.utils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jianglei on 16/7/30.
 */

public class ObservableUtil {
    public static <T> void runOnUI(Observable.OnSubscribe<T> observable, Subscriber<T> subscriber) {
        Observable.create(observable).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
