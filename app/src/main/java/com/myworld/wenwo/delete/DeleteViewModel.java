package com.myworld.wenwo.delete;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.utils.ObservableUtil;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/11.
 */

public class DeleteViewModel implements ViewModel {
    private AskMe askMe;
    private Context context;
    public ObservableField<String> reason = new ObservableField<>();
    private ProgressDialog dialog;
    public ReplyCommand deleteClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(reason.get())) {
                Toast.makeText(context, context.getString(R.string.reason_not_empty), Toast.LENGTH_SHORT).show();
                return;
            }
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.delete_ing));
            dialog.show();
            deleteAsk();
        }
    });

    public DeleteViewModel(AskMe askMe, Context context) {
        this.askMe = askMe;
        this.context = context;
    }

    public void deleteAsk() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(AskMeRepository.getInstance().deleteAsk(askMe, reason.get(), Config.USERNAME));
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    EventBus.getInstance().onEevent(DeleteActivity.class, "deleteComplete");
                } else {
                    Toast.makeText(context, context.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
}
