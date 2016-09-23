package com.myworld.wenwo.data.source.remote;

import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.BannerItem;
import com.youth.banner.Banner;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jianglei on 16/7/11.
 */

public interface AskMeService {
    @GET("/ask/allask")
    Call<List<AskMe>> getAskList(@Query("username") String userName, @Query("status") int type, @Query("page") int page, @Query("size") int size, @Query("position_geo") String position, @Query("range") int range);

    @GET("/ask/gettagask")
    Call<List<AskMe>> getTagAskList(@Query("username") String userName, @Query("status") int type, @Query("page") int page, @Query("size") int size, @Query("tag") String tag);

    @GET("/ask/getask")
    Call<AskMe> getAsk(@Query("ask_id") String objectId);

    @FormUrlEncoded
    @POST("/ask/askdetail")
    Call<String> getAskDetail(@Field("username") String userName, @Field("ask_id") String objectId);

    @FormUrlEncoded
    @POST("/hode/haved")
    Call<List<AskMe>> getHavedAsk(@Field("username") String userName, @Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST("/hode/foodlikelist")
    Call<List<AskMe>> getLikedLisk(@Field("username") String userName, @Field("page") int page, @Field("size") int size);

    @FormUrlEncoded
    @POST("/hode/foodlike")
    Call<Boolean> like(@Field("username") String userName, @Field("ask_id") String objectId);

    @FormUrlEncoded
    @POST("/hode/cancelfoodlike")
    Call<Boolean> dislike(@Field("username") String userName, @Field("ask_id") String objectId);

    @FormUrlEncoded
    @POST("/ask/sendask")
    Call<AskMe> sendAsk(@Field("username") String userName, @Field("ask_id") String askId, @Field("type") int type, @Field("price") float price, @Field("geo_x") double geoX, @Field("geo_y") double geoY, @Field("position") String position, @Field("reason") String reason
            , @Field("content_show") String contentShow, @Field("tag") String title, @Field("shop_name") String shopName, @Field("images") String images);

    @GET("/ask/myask")
    Call<List<AskMe>> getMyAskList(@Query("username") String userName, @Query("page") int page, @Query("size") int size);

    @GET("/carousel/getcarouselinfo")
    Call<List<BannerItem>> getBanner(@Query("type") String type, @Query("username") String userName);

    @FormUrlEncoded
    @POST("/ask/askedit")
    Call<Boolean> editAsk(@Field("username") String userName, @Field("ask_id") String askId, @Field("type") int type, @Field("price") float price, @Field("geo_x") double geoX, @Field("geo_y") double geoY, @Field("position") String position, @Field("reason") String reason
            , @Field("content_show") String contentShow, @Field("tag") String title, @Field("shop_name") String shopName, @Field("images") String images);

    @FormUrlEncoded
    @POST("/ask/del")
    Call<Boolean> deleteAsk(@Field("ask_id") String objectId, @Field("username") String userName, @Field("reason") String reason);

    @GET("/authorization/pay")
    Call<String> getOrder(@Query("openid") String openId, @Query("access_token") String token, @Query("expires_in") int expiresIn, @Query("port") String port, @Query("ask_id") String objectId, @Query("username") String userName);

    @FormUrlEncoded
    @POST("/ask/addLook")
    Call<Boolean> addLook(@Field("ask_id") String objectId);

    @FormUrlEncoded
    @POST("/ask/debase")
    Call<Boolean> debase(@Field("ask_id") String objectId, @Field("username") String userName, @Field("content") String content);

    @GET("/ask/getalltag")
    Call<String> getAllTag();

    @GET("/card/getcardlist")
    Call<String> getCards(@Query("username") String userName);

    @FormUrlEncoded
    @POST("/card/addDownNum")
    Call<Boolean> addDownNum(@Field("card_id") String cardId);

    @GET("/location/gethotlist")
    Call<String> getHostAddress();

    @GET("/ask/skeyword")
    Call<String> getKeyWords(@Query("keyword") String keyword);

    @GET("/ask/search")
    Call<List<AskMe>> searchAsk(@Query("username") String userName, @Query("page") int page, @Query("size") int size, @Query("keyword") String keyword);

    @GET("/manage/addlookuser")
    Call<Boolean> addLookUser(@Query("username") String userName);

    @FormUrlEncoded
    @POST("/ask/autosendask")
    Call<AskMe> autoSendAsk(@Field("username") String userName, @Field("ask_id") String askId
            , @Field("type") int type, @Field("price") float price, @Field("geo_x") double geoX
            , @Field("geo_y") double geoY, @Field("position") String position, @Field("reason") String reason
            , @Field("content_show") String contentShow, @Field("tag") String title, @Field("shop_name") String shopName
            , @Field("images") String images, @Field("external_link") String externalLink);


}
