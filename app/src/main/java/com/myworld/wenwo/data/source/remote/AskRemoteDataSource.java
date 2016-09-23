package com.myworld.wenwo.data.source.remote;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.AskMeDataSource;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.BannerItem;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by jianglei on 16/7/11.
 */

public class AskRemoteDataSource implements AskMeDataSource {
    private static AskRemoteDataSource instance;
    private AskMeService service;

    public static AskRemoteDataSource getInstance() {
        if (instance == null)
            instance = new AskRemoteDataSource();
        return instance;
    }

    private AskRemoteDataSource() {
        service = RetrofitProvider.create().create(AskMeService.class);
    }

    @Override
    public AskMe getAsk(String objectId) {
        try {
            return service.getAsk(objectId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AskMe> getAllAsk(int type, int page, int size) {
        String position = Config.geo_latitude + "," + Config.geo_longtitude;
        try {
            Response<List<AskMe>> response = service.getAskList(Config.USERNAME, type, page, size, position, Config.range).execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<AskMe> getTagAsk(String userName, int type, int page, int size, String tag) {
        try {
            return service.getTagAskList(userName, type, page, size, tag).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<AskMe> getLikedList(String userName, int page) {
        try {
            return service.getLikedLisk(userName, page, Config.PAGE_SIZE).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    @Override
    public boolean like(String usrName, String objectId) {
        try {
            return service.like(usrName, objectId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dislike(String usrName, String objectId) {
        try {
            return service.dislike(usrName, objectId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean sendAsk(AskMe askMe, String userName) {
        askMe.setCreateByName(userName);
        try {
            AskMe result = service.sendAsk(askMe.getCreateByName(), askMe.getObjectId(), askMe.getAskType(), askMe.getAskPrice(), askMe.getGeoX(), askMe.getGeoY()
                    , askMe.getAskPosition(), askMe.getAskReason(), askMe.getAskContentShow(), askMe.getAskTagStr(), askMe.getShopName(), askMe.getAskImage()).execute().body();
            if (result != null)
                return !TextUtils.isEmpty(result.getObjectId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean autoSendAsk(AskMe askMe, String userName) {
        askMe.setCreateByName(userName);
        try {
            AskMe result = service.autoSendAsk(askMe.getCreateByName(), askMe.getObjectId(), askMe.getAskType(), askMe.getAskPrice(), askMe.getGeoX(), askMe.getGeoY()
                    , askMe.getAskPosition(), askMe.getAskReason(), askMe.getAskContentShow(), askMe.getAskTagStr(), askMe.getShopName(), askMe.getAskImage(), askMe.getAskDefault()).execute().body();
            if (result != null && result.getObjectId() != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AskMe getAskDetail(String userName, String objectId) {
        try {
            String result = service.getAskDetail(userName, objectId).execute().body();
            AskMe askMe = RetrofitProvider.gson.fromJson(result, AskMe.class);
            JSONObject root = new JSONObject(result);
            int havedFlag = root.getJSONObject("show").getInt("havedFlag");
            int foodLikeFlag = root.getJSONObject("show").getInt("foodLikeFlag");
            askMe.setLiked(foodLikeFlag);
            askMe.setHaved(havedFlag > 0);
            return askMe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AskMe> getMyAskList(String userName, int page) {
        try {
            return service.getMyAskList(userName, page, Config.PAGE_SIZE).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AskMe> getHavedList(String userName, int page) {
        try {
            return service.getHavedAsk(userName, page, Config.PAGE_SIZE).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean editAsk(AskMe askMe, String userName) {
        try {
            return service.editAsk(askMe.getCreateByName(), askMe.getObjectId(), askMe.getAskType(), askMe.getAskPrice(), askMe.getGeoX(), askMe.getGeoY()
                    , askMe.getAskPosition(), askMe.getAskReason(), askMe.getAskContentShow(), askMe.getAskTagStr(), askMe.getShopName(), askMe.getAskImage()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getOrder(String openId, String accessToken, int expirseIn, String objectid, String userName) {
        try {
            return service.getOrder(openId, accessToken, expirseIn, "app", objectid, userName).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean deleteAsk(AskMe askMe, String reason, String userName) {
        try {
            return service.deleteAsk(askMe.getObjectId(), userName, reason).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BannerItem> getBanner() {
        try {
            return service.getBanner("", "").execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BannerItem> getTopic(String userName) {
        try {
            return service.getBanner("all", userName).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean addLook(String objectId) {
        try {
            return service.addLook(objectId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean debase(String objectId, String userName, String content) {
        try {
            return service.debase(objectId, userName, content).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getAllTag() {
        try {
            return service.getAllTag().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getCards(String userName) {
        try {
            return service.getCards(userName).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getHostAddress() {
        try {
            return service.getHostAddress().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getKeyWords(String keyword) {
        try {
            return service.getKeyWords(keyword).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<AskMe> searchAsk(String userName, int page, int size, String keyword) {
        try {
            return service.searchAsk(userName, page, size, keyword).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    @Override
    public boolean addCardDownNum(String cardId) {
        try {
            return service.addDownNum(cardId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addLookUser(String userId) {
        try {
            return service.addLookUser(userId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
