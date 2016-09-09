package com.myworld.wenwo.data.source.remote.user;

import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jianglei on 16/7/29.
 */

public interface UserService {
    @GET("/user/regist")
    public Call<Boolean> regist(@Query("username") String userName, @Query("password") String password, @Query("userhead") String userHead);

    @FormUrlEncoded
    @POST("/user/login")
    public Call<String> login(@Field("openid") String openId, @Field("access_token") String token, @Field("expires_in") int expiresIn);

    @FormUrlEncoded
    @POST("/user/getinfo")
    public Call<UserInfo> getUserInfo(@Field("username") String userName);

    @GET("/user/getuserinfo")
    public Call<User> getUser(@Query("username") String userName);

}
