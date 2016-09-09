package com.myworld.wenwo.add;

import android.content.Context;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.base.AddItemViewModel;
import com.myworld.wenwo.application.WenWoApplication;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by jianglei on 16/7/20.
 */

public class ReasonItemViewModel extends AddItemViewModel {
    public ObservableField<String> content = new ObservableField<>();
    public ReplyCommand eg1Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            content.set(WenWoApplication.getInstance().getString(R.string.reason_tip_1).replace("eg:",""));
        }
    });
    public ReplyCommand eg2Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            content.set(WenWoApplication.getInstance().getString(R.string.reason_tip_2).replace("eg:",""));
        }
    });
    public ReplyCommand eg3Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            content.set(WenWoApplication.getInstance().getString(R.string.reason_tip_3).replace("eg:",""));
        }
    });
    public ReplyCommand eg4Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            content.set(WenWoApplication.getInstance().getString(R.string.reason_tip_4).replace("eg:",""));
        }
    });
    @Override
    public String getContentText() {
        return content.get();
    }
    private Context context;

    public ReasonItemViewModel(Context context) {
        this.context = context;
    }
}
