package com.myworld.wenwo.register;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.infoedit.InfoEditActivity;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/4.
 */

public class RegisterViewModel implements ViewModel {
    public ObservableBoolean confirmed = new ObservableBoolean(false);
    public ObservableBoolean codeSend = new ObservableBoolean(false);
    private Context context;
    public ReplyCommand nextClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!codeSend.get()) {
                codeSend.set(true);
            } else {
                checkCode();
            }
        }
    });

    private void checkCode() {
        Intent intent = new Intent(context, InfoEditActivity.class);
        context.startActivity(intent);
    }

    public RegisterViewModel(Context context) {
        this.context = context;
    }
}
