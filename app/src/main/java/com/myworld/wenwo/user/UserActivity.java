package com.myworld.wenwo.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.databinding.ActivityUserBinding;
import com.myworld.wenwo.login.LoginActivity;
import com.myworld.wenwo.setting.SettingsActivity;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.view.widget.TitleBar;

import rx.Observable;
import rx.Subscriber;

public class UserActivity extends AppCompatActivity {
    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        updateUser();
        EventBus.getInstance().regist(this);
    }

    private void initUserHead(UserInfo userInfo) {
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.user_image);
        draweeView.setImageURI(userInfo.getUserHead());
    }

    @OnEvent("updateUser")
    public void updateUser() {
        if (!TextUtils.isEmpty(Config.USERNAME))
            ObservableUtil.runOnUI(new Observable.OnSubscribe<UserInfo>() {
                @Override
                public void call(Subscriber<? super UserInfo> subscriber) {
                    subscriber.onNext(UserRepository.getInstance().getUserInfoFromCache(getApplicationContext(),Config.USERNAME));
                }
            }, new Subscriber<UserInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(UserInfo userInfo) {
                    updateOnline();
                    binding.setUserViewModel(new UserViewModel(UserActivity.this, userInfo));
                    initUserHead(userInfo);
                }
            });
        else {
            binding.setUserViewModel(new UserViewModel(UserActivity.this, null));
            findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    public void updateOnline(){
        if (!TextUtils.isEmpty(Config.USERNAME))
            ObservableUtil.runOnUI(new Observable.OnSubscribe<UserInfo>() {
                @Override
                public void call(Subscriber<? super UserInfo> subscriber) {
                    subscriber.onNext(UserRepository.getInstance().getUserInfo(getApplicationContext(),Config.USERNAME));
                }
            }, new Subscriber<UserInfo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(UserInfo userInfo) {
                    binding.setUserViewModel(new UserViewModel(UserActivity.this, userInfo));
                    initUserHead(userInfo);
                }
            });
        else {
            binding.setUserViewModel(new UserViewModel(UserActivity.this, null));
            findViewById(R.id.header_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private static final String TAG = "UserActivity";
}
