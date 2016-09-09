package com.myworld.wenwo.data.source.remote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jianglei on 16/8/12.
 */

public interface PayService {
    @FormUrlEncoded
    @POST("/pay/unifiedorder")
    public Call<String> getOrder(@Field("appid") String appId, @Field("body") String body, @Field("much_id") String muchId, @Field("nonce_str") String nonceStr, @Field("out_trade_no") String outTradeNO, @Field("spbill_create_ip") String ip,
                                 @Field("total_fee") int totalFee, @Field("trade_type") String tradeType, @Field("sign") String sign);
}
