package com.myworld.wenwo;

import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.data.source.remote.AskMeService;
import com.myworld.wenwo.data.source.remote.AskRemoteDataSource;
import com.myworld.wenwo.data.source.remote.PayService;
import com.myworld.wenwo.data.source.remote.RetrofitProvider;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void remoteTest() {
        List<AskMe> results = AskRemoteDataSource.getInstance().getAllAsk(1, 0, 20);
        for (AskMe ask : results) {
            System.out.println(ask.toString());
        }
    }

    @Test
    public void likeTest() {
        boolean success = AskRemoteDataSource.getInstance().dislike("573b0e3df38c8400673bb48d", "579741eaa34131005aa06b650");
        System.out.print(success);
    }

    @Test
    public void getOrder() {
        PayService service=RetrofitProvider.create("https://api.mch.weixin.qq.com").create(PayService.class);
        String appId="wxb4b71b749fb0ea09";
        String body="test";
        String muchId="1373092602";
        String nonceStr=String.valueOf(Math.random());
//        String notifyUrl=

//        String result=service.getOrder();
    }

    @Test
    public void havedTest() {
        AskMeService service = RetrofitProvider.create().create(AskMeService.class);
        try {
            String result = service.like("573b0e3df38c8400673bb48d", "579741eaa34131005aa06b650").execute().raw().body().string();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            List<AskMe> results = service.getLikedLisk("573b0e3df38c8400673bb48d", 0, 20).execute().body();
            for (AskMe ask : results) {
                System.out.println(ask.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void userTest() {
        UserInfo userInfo = UserRepository.getInstance().getUserInfo(Config.USERNAME);
        System.out.print(userInfo.toString());
    }

    @Test
    public void senTest() {
        AskMe askMe = new AskMe();
        askMe.setCreateByName("573b0e3df38c8400673bb48d");
        askMe.setAskContentShow("{\"detail\":\"[leojiang's test]汤底包括所有的菜品都可以只点半份，很人性化。清油红锅味道一般，但是番茄汤很出彩。服务就不用说了，出了名的好，深入人心，让人上瘾，所谓的服务有毒啊！排了整整两小时，排队时饮料小吃供应，提供照片打印、擦鞋、美甲服务，小孩子游乐区，中途还有变脸节目和抽奖活动。\",\"detailLi\":\"[{\\\"name\\\":\\\"人均消费\\\",\\\"val\\\":\\\"100元左右\\\"}]\"}");
        askMe.setAskPrice(4.99f);
        askMe.setAskPosition("{\"province\":\"四川省\",\"city\":\"成都市\",\"district\":\"成华区\",\"township\":\"建设路街道\",\"detail\":\"颐和家园颐和家园(建和路)\"}");
        askMe.setAskTagStr("[{\"tag_name\":\"火锅\"}]");
        askMe.setShopName("leojiang");
        askMe.setAskReason("heloow");
        AskRemoteDataSource.getInstance().sendAsk(askMe, "573b0e3df38c8400673bb48d");
    }
}