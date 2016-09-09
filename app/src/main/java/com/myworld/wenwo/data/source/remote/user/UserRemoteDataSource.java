package com.myworld.wenwo.data.source.remote.user;

import android.content.Context;

import com.myworld.wenwo.data.UserDataSource;
import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.source.remote.RetrofitProvider;

import java.io.IOException;

/**
 * Created by jianglei on 16/7/29.
 */

public class UserRemoteDataSource implements UserDataSource {
    private static UserRemoteDataSource instance;
    private UserService service;

    public static UserRemoteDataSource getInstance() {
        if (instance == null)
            instance = new UserRemoteDataSource();
        return instance;
    }

    public UserRemoteDataSource() {
        service = RetrofitProvider.create().create(UserService.class);
    }

    @Override
    public String login(String openId, String token, int expiresIn) {
        try {
            return service.login(openId, token, expiresIn).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public User getUser(String userName) {
        try {
            return service.getUser(userName).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserInfo getUserInfo(String userName) {
        try {
            return service.getUserInfo(userName).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean regist(String userName, String password, String userHead) {
        try {
            return service.regist(userName, password, userHead).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserInfo getUserInfo(Context context, String userName) {
        return null;
    }

    @Override
    public UserInfo getUserInfoFromCache(Context context, String userName) {
        return null;
    }
}
