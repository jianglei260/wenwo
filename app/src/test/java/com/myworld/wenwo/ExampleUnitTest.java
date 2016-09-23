package com.myworld.wenwo;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.DetialTag;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.data.source.remote.AskMeService;
import com.myworld.wenwo.data.source.remote.AskRemoteDataSource;
import com.myworld.wenwo.data.source.remote.PayService;
import com.myworld.wenwo.data.source.remote.RetrofitProvider;
import com.myworld.wenwo.detial.DetialTagViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


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
        PayService service = RetrofitProvider.create("https://api.mch.weixin.qq.com").create(PayService.class);
        String appId = "wxb4b71b749fb0ea09";
        String body = "test";
        String muchId = "1373092602";
        String nonceStr = String.valueOf(Math.random());
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

    private static HashMap<String, Integer> type = new HashMap<>();

    static {
        type.put("甜点饮品", 11);
        type.put("生日蛋糕", 20097);
        type.put("火锅", 17);
        type.put("自助餐", 40);//ok
        type.put("小吃快餐", 36);//ok
        type.put("日韩料理", 28);//ok
        type.put("西餐", 35);//ok
        type.put("聚餐宴请", 395);
        type.put("烧烤烤肉", 54);
        type.put("川湘菜", 55);
        type.put("江浙菜", 56);
        type.put("东北菜", 20003);
        type.put("香锅烤鱼", 20004);
        type.put("粤菜", 57);
        type.put("中式烧烤／烤串", 400);
        type.put("咖啡酒吧", 41);
        type.put("京菜鲁菜", 59);
        type.put("云贵菜", 60);
        type.put("东南亚菜", 62);
        type.put("海鲜", 63);
        type.put("素食", 217);
        type.put("台湾／客家菜", 227);
        type.put("创意菜", 228);
        type.put("汤／粥／炖菜", 229);
        type.put("蒙餐", 232);
        type.put("新疆菜", 233);
        type.put("其它美食", 24);

    }

    @Test
    public void catchFood() {
        String CATCH_TYPE = "西餐";
        int cid = type.get(CATCH_TYPE);
        startMeituan(CATCH_TYPE, cid);
    }

    private int index = 0;
    int page = 0;

    public void startMeituan(String catchType, int cid) {
        OkHttpClient client = new OkHttpClient();
        StringBuilder builder = new StringBuilder();
        java.text.DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        builder.append(dateFormat.format(date));
        builder.append("\n");
        Request firstRequest = new Request
                .Builder().url("http://i.meituan.com/chengdu?cid=" + cid + "&sid=rating&cateType=poi&stid_b=1").build();
        page = 0;
        index = 0;
        while ((page = meituan(client, firstRequest, catchType, cid, builder)) > 0) {
            firstRequest = new Request
                    .Builder().url("http://i.meituan.com/chengdu?cid=" + cid + "&p=" + page + "&sid=rating&cateType=poi&stid_b=1").build();
        }

    }

    public int meituan(OkHttpClient client, Request getRequest, String catchType, int cid, StringBuilder builder) {
        try {
            String result = client.newCall(getRequest).execute().body().string();
            Document doc = Jsoup.parse(result);
            Element listDiv = doc.getElementsByClass("deals-list").first();
            Elements listDl = listDiv.getElementsByAttributeValue("gaevent", "common/poilist");
            for (Element element : listDl) {
                float star = Float.valueOf(element.getElementsByClass("star-text").first().text());
                String name = element.getElementsByClass("poiname").text();
                String url = element.getElementsByClass("poi-list-item").first().getElementsByClass("react").first().attr("href");
                Element geoElement = element.getElementsByAttribute("data-lat").first();
                double geoX = Double.valueOf(geoElement.attr("data-lat"));
                double geoY = Double.valueOf(geoElement.attr("data-lng"));
                if (star >= 4.5) {
                    AskMe askMe = meituanDetail(client, url);
                    if (askMe == null)
                        continue;
                    askMe.setShopName(name);
                    askMe.setGeoX(geoX);
                    askMe.setGeoY(geoY);
                    askMe.setAskReason("后台审核时添加");
                    askMe.setAskTagStr("[{\"tag_name\":\"" + catchType + "\"}]");
                    askMe.setAskDefault(url);
                    System.out.println(askMe.getAskPosition());
                    boolean success = AskRemoteDataSource.getInstance().autoSendAsk(askMe, askMe.getCreateByName());
                    builder.append("序列：" + index + "页面：" + page + " 店名:" + name + " 评分" + star + " 链接:" + url + " 分类:" + catchType + " cid:" + cid + " 状态：" + success + "\n");
                    index++;
                } else {
                    return -1;
                }
                System.out.println(name + " 评分" + star + " 链接:" + url);
            }
            Elements nextPage = listDiv.getElementsByAttributeValue("gaevent", "imt/deal/list/pageNext");
            if (nextPage != null && nextPage.size() > 0) {
                return Integer.valueOf(nextPage.first().attr("data-page-num"));
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLog(builder.toString());
            builder.delete(0, builder.length());
        }
        return -1;
    }

    public void writeLog(String log) {
        File logFile = new File("catch_2.0.log");
        try {
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(log);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public AskMe meituanDetail(OkHttpClient client, String url) {
        Request getRequest = new Request
                .Builder().url(url).build();
        try {
            String result = client.newCall(getRequest).execute().body().string();
            Document doc = Jsoup.parse(result);
            Element poiDl = doc.getElementsByAttributeValue("class", "list poi-info-wrapper").first();

            String avgPrice = poiDl.getElementsByClass("avg-price").first().text();
            String address = poiDl.getElementsByClass("poi-address").first().text();
            Elements recommandDd = doc.getElementsByAttributeValue("class", "dd-padding recommond-wrapper");
            StringBuilder builder = new StringBuilder();
            if (recommandDd != null && recommandDd.size() > 0) {
                Elements recommands = recommandDd.first().getElementsByAttributeValue("class", "recommond-item");
                int size = recommands.size();
                for (int i = 0; i < size; i++) {
                    if (i >= 5)
                        break;
                    String recommand = recommands.get(i).text();
                    builder.append(recommand);
                    if (i < 4 && i < size - 1)
                        builder.append("、");
                }
            }
            String feedbackUrl = url.replace("/poi", "/poi/feedbacks");
            Request feedbackRequest = new Request
                    .Builder().url(feedbackUrl).build();
            String feedbackResult = client.newCall(feedbackRequest).execute().body().string();

            Document feedbackDoc = Jsoup.parse(feedbackResult);
            Elements feedbackDiv = feedbackDoc.getElementsByAttributeValue("class", "feedbackCard");
            String images = null;
            String commentText = null;
            if (feedbackDiv != null && feedbackDiv.size() > 0) {
                Element comment = feedbackDiv.first().getElementsByAttributeValue("class", "comment").first();
                commentText = comment.getElementsByTag("p").text();
                Elements image = feedbackDiv.first().getElementsByAttributeValue("class", "pics");
                if (image != null && image.size() > 0) {
                    images = image.first().attr("data-pics");
                }
            }
            Element poiSummary = feedbackDoc.getElementById("poi-summary");
            Elements poiElements = poiSummary.getElementsByAttributeValue("class", "dd-padding kv-line");
            String desc = null;
            if (poiSummary != null) {
                int size = poiElements.size();
                for (int i = 0; i < size; i++) {
                    Elements doorDesc = poiElements.get(i).getElementsContainingText("门店介绍");
                    if (doorDesc != null && doorDesc.size() > 0) {
                        desc = doorDesc.first().getElementsByTag("p").text();
                    }
                }
            }
            String recommandVal = builder.toString();
            System.out.println("平均消费: " + avgPrice);
            System.out.println("地址: " + address);
            System.out.println("推荐菜: " + recommandVal);
            System.out.println("图片: " + images);
            System.out.println("评价: " + commentText);
            System.out.println("店铺: " + desc);

            if (commentText == null && desc == null)
                return null;

            AskMe askMe = new AskMe();
            askMe.setCreateByName("573b0e3df38c8400673bb48d");
            askMe.setAskPosition(getAddressJson(address));
            List<DetialTag> tags = new ArrayList<>();
            DetialTag avgtag = new DetialTag();
            avgtag.setName("人均消费");
            avgtag.setVal(avgPrice);
            if (avgPrice != null && avgPrice.length() > 0)
                tags.add(avgtag);
            DetialTag commandtag = new DetialTag();
            commandtag.setName("推荐菜");
            commandtag.setVal(recommandVal);
            if (recommandVal != null && recommandVal.length() > 0)
                tags.add(commandtag);
            askMe.setAskContentShow(getContentStr(commentText, tags));
            if (commentText == null || commentText.length() <= 0)
                askMe.setAskContentShow(getContentStr(desc, tags));
            askMe.setAskImage(getImages(images));
            return askMe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImages(String imageUrl) {
        if (imageUrl == null || imageUrl.length() == 0)
            return "";
        String[] images = imageUrl.split(",");
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < images.length; i++) {
            builder.append("{\"image\":\"");
            builder.append(images[i]);
            builder.append("\"}");
            if (i < images.length - 1)
                builder.append(",");
        }
        builder.append("]");
        return builder.toString();
    }

    public String getContentStr(String detail, List<DetialTag> tags) {
        StringBuilder builder = new StringBuilder();
        int size = tags.size();
        for (int i = 0; i < size; i++) {
            DetialTag tag = tags.get(i);
            String name = tag.getName();
            String val = tag.getVal();
            builder.append("{\"name\":\"");
            builder.append(name);
            builder.append("\",");
            builder.append("\"val\":\"");
            builder.append(val);
            builder.append("\"}");
            if (i < size - 1) {
                builder.append(",");
            }
        }
        String contentJson = "{\"detail\":\"" + detail + "\",\"detailLi\":[" + builder.toString() + "]}";
        return contentJson;
    }

    public String getAddressJson(String addr) {
        String addressJson = "{\"province\":\"四川省\",\"city\":\"成都市\",\"district\":\"" + addr + "\",\"township\":\"\",\"detail\":\"\",\"adetail\":\"\"}";
        return addressJson;
    }

    @Test
    public void nuomi() {
        FormBody.Builder builder = new FormBody.Builder().add("version", "v1").add("city", "chengdu");
        Request request = new Request
                .Builder().url("http://api.nuomi.com/api/dailydeal").post(builder.build()).build();
        OkHttpClient client = new OkHttpClient();
        Request getRequest = new Request
                .Builder().url("http://api.nuomi.com/api/dailydeal?version=v1&city=chengdu").build();
        byte[] buffer = new byte[4096];
        File file = new File("nuomi_chengdu.xml");
        try {
            InputStream in = client.newCall(getRequest).execute().body().byteStream();
            FileOutputStream fos = new FileOutputStream(file);
            while ((in.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer);
            }
            in.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//
//            String result = client.newCall(getRequest).execute().body().string();
//            System.out.print(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void renameFile() {
        File file = new File("/Users/jianglei/Downloads/create");
        File[] images = file.listFiles();
        for (int i = 0; i < images.length; i++) {
            File newName = new File(file, "wenwo" + i + ".jpg");
            images[i].renameTo(newName);
        }
    }
}