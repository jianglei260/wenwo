package com.myworld.wenwo.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.UserDataSource;
import com.myworld.wenwo.data.entity.AuthInfo;
import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.source.remote.user.UserRemoteDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by jianglei on 16/7/29.
 */

public class UserRepository implements UserDataSource {
    private static UserRepository instance;
    private UserRemoteDataSource remote;
    public static final String USER = "user";

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    UserRepository() {
        remote = UserRemoteDataSource.getInstance();
    }

    @Override
    public String login(String openId, String token, int expiresIn) {
        return remote.login(openId, token, expiresIn);
    }

    @Override
    public User getUser(String userName) {
        return remote.getUser(userName);
    }

    @Override
    public UserInfo getUserInfo(Context context, String userName) {
        UserInfo info = remote.getUserInfo(userName);
        updateLocalUserInfo(context, info);
        return info;
    }

    @Override
    public UserInfo getUserInfoFromCache(Context context, String userName) {
        UserInfo userInfo = getLocalUserInfo(context);
//        if (Config.USERNAME.equals(userInfo.))
        if (userInfo == null)
            userInfo = getUserInfo(context, userName);
        return userInfo;
    }

    public void saveUserToLocal(Context context, String userName) {
        SharedPreferences preferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        preferences.edit().putString("userName", userName).commit();
    }

    public static UserInfo getLocalUserInfo(Context context) {
        Gson gson = new Gson();
        String authDataPath = context.getFilesDir().getAbsolutePath() + "/userinfo.json";
        File authData = new File(authDataPath);
        if (!authData.exists())
            return null;
        try {
            Reader reader = new FileReader(authData);
            return gson.fromJson(reader, UserInfo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLocalUserInfo(Context context, UserInfo userInfo) {
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        String authDataPath = context.getFilesDir().getAbsolutePath() + "/userinfo.json";
        File authData = new File(authDataPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(authData, false);
            byte[] data = json.getBytes();
            fos.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserFromLocal(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        return preferences.getString("userName", "");
    }

    @Override
    public boolean regist(String userName, String password, String userHead) {
        return remote.regist(userName, password, userHead);
    }
}
