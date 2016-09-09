package com.myworld.wenwo.card;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.detial.DetialActivity;
import com.myworld.wenwo.found.LikeStateChangedListener;
import com.myworld.wenwo.login.CheckForLogin;
import com.myworld.wenwo.utils.ActivityTool;
import com.myworld.wenwo.utils.ObservableUtil;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/29.
 */

public class CardItemViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> descText = new ObservableField<>();
    public ObservableField<String> author = new ObservableField<>();
    public ObservableField<String> objectId=new ObservableField<>();
    public ObservableField<String> cardId=new ObservableField<>();
    public ObservableInt likeNum=new ObservableInt();
    public ObservableInt downNum=new ObservableInt();
    public ObservableBoolean liked=new ObservableBoolean();
    public ObservableBoolean sloganVisibility = new ObservableBoolean(false);
    public CardItemViewModel(Context context) {
        this.context = context;
    }
    public ReplyCommand lookClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, DetialActivity.class);
            intent.putExtra(DetialActivity.OBJTCT_ID, objectId.get());
            ActivityTool.startActivity(context, DetialActivity.class, intent, new LikeStateChangedListener() {
                @Override
                public void liked(int position) {
                    liked.set(true);
                    likeNum.set(likeNum.get()+1);
                }

                @Override
                public void disLiked(int position) {
                    liked.set(false);
                    likeNum.set(likeNum.get()-1);
                }
            });
        }
    });
    public ReplyCommand likeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(Config.USERNAME)) {
                CheckForLogin.needLogin(context);
                return;
            }
            if (liked.get())
                cancelLike();
            else
                like();
        }
    });

    protected void like() {
        liked.set(true);
        likeNum.set(likeNum.get()+1);
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean success = AskMeRepository.getInstance().like(Config.USERNAME, objectId.get());
                subscriber.onNext(success);
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean success) {
                if (success) {
                    liked.set(true);
                } else {
                    liked.set(false);
                    likeNum.set(likeNum.get()-1);
                }

            }
        });
    }


    protected void cancelLike() {
        liked.set(false);
        likeNum.set(likeNum.get()-1);
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean success = AskMeRepository.getInstance().dislike(Config.USERNAME, objectId.get());
                subscriber.onNext(success);
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean success) {
                if (success) {
                    liked.set(false);
                } else {
                    liked.set(true);
                    likeNum.set(likeNum.get()+1);
                }

            }
        });
    }


}
