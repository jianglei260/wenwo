package com.myworld.wenwo.data;

import android.content.Context;

import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;

/**
 * Created by jianglei on 16/7/29.
 */

public interface UserDataSource {
    public String login(String openId, String token, int expiresIn);

    public User getUser(String userName);

    public boolean regist(String userName, String password, String userHead);

    public UserInfo getUserInfo(Context context,String userName);

    public UserInfo getUserInfoFromCache(Context context,String userName);
}
