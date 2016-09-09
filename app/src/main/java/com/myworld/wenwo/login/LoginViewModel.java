package com.myworld.wenwo.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.data.entity.AuthInfo;
import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.data.source.local.AuthLocalDataSource;
import com.myworld.wenwo.register.RegisterActivity;
import com.myworld.wenwo.user.UserActivity;
import com.myworld.wenwo.view.widget.WenwoDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by jianglei on 16/7/29.
 */

public class LoginViewModel implements ViewModel, PlatformActionListener {
    private Context context;
    private WenwoDialog dialog;

    public ReplyCommand wechatLogin = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Wechat wechat = new Wechat(context);
            doLogin(wechat);
        }
    });

    public ReplyCommand registerClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
    });

    public LoginViewModel(Context context) {
        this.context = context;
    }

    private void doLogin(Platform platform) {
        AuthInfo info = AuthLocalDataSource.getAuthInfo(context);
        if (info != null)
            Log.d("info", info.toString());
        if (platform.isAuthValid()) {
            Log.d("success", platform.getDb().exportData());
            loginSuccess(platform);
            return;
        }
        platform.getDb().removeAccount();
        platform.removeAccount(true);
        platform.setPlatformActionListener(this);
        platform.SSOSetting(false);
        platform.showUser("");
        Toast.makeText(context, context.getString(R.string.open_weixin), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.d("success", platform.getDb().exportData());
        loginSuccess(platform);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.d("error:", throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.d("cancel:", platform.getName());
    }

    private void loginSuccess(final Platform platform) {
        Observable.create(new Observable.OnSubscribe<AuthInfo>() {
            @Override
            public void call(Subscriber<? super AuthInfo> subscriber) {
                Gson gson = new Gson();
                AuthInfo authInfo = gson.fromJson(platform.getDb().exportData(), AuthInfo.class);
                String result = UserRepository.getInstance().login(authInfo.getOpenid(), authInfo.getToken(), authInfo.getExpiresIn());
                if (!TextUtils.isEmpty(result)) {
                    try {
                        String userName = new JSONObject(result).getJSONObject("data").getString("username");
                        UserRepository.getInstance().saveUserToLocal(context, userName);
                        Config.USERNAME = userName;
                        AuthLocalDataSource.saveAuthInfo(context, platform.getDb().exportData());
                        subscriber.onNext(AuthLocalDataSource.getAuthInfo(context));
                        subscriber.onCompleted();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AuthInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AuthInfo authInfo) {
                EventBus.getInstance().onEevent(UserActivity.class, "updateUser");
                ((LoginActivity) context).finish();
            }
        });
    }
}
