package com.myworld.wenwo.infoedit;

import android.content.Context;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/4.
 */

public class InfoEditViewModel implements ViewModel {
    private Context context;
    public ReplyCommand start = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            uploadInfo();

        }
    });

    private void uploadInfo() {

    }

    public InfoEditViewModel(Context context) {
        this.context = context;
    }
}
