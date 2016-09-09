package com.myworld.wenwo.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.AskMeDataSource;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.AuthInfo;
import com.myworld.wenwo.data.entity.BannerItem;
import com.myworld.wenwo.data.source.local.AskLocalDataSource;
import com.myworld.wenwo.data.source.remote.ApiTypeAdapterFactory;
import com.myworld.wenwo.data.source.remote.AskRemoteDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jianglei on 16/7/11.
 */

public class AskMeRepository implements AskMeDataSource {
    private static AskMeRepository repository;
    private AskLocalDataSource local;
    private AskRemoteDataSource remote;
    private HashMap<String, AskMe> cache;
    private boolean isCacheDirty = false;

    public static AskMeRepository getInstance() {
        if (repository == null) {
            repository = new AskMeRepository();
        }

        return repository;
    }

    private AskMeRepository() {
        this.remote = AskRemoteDataSource.getInstance();
        this.local = AskLocalDataSource.getInstance();
        cache = new HashMap<>();
    }

    @Override
    public AskMe getAsk(String objectId) {
        AskMe askMe = cache.get(objectId);
        if (askMe == null || isCacheDirty)
            try {
                askMe = local.getAsk(objectId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (askMe == null)
            askMe = getAskDetail(Config.USERNAME, objectId);
        return askMe;
    }

    @Override
    public List<AskMe> getAllAsk(int type, int page, int size) {
        List<AskMe> results = getCached();
        if (results == null || results.isEmpty() || isCacheDirty)
            results = getAllFromLocal();
        if (results == null || results.isEmpty() || page > 0)
            results = getAllFromRemote(type, page, size);
        return results;
    }

    @Override
    public List<AskMe> getTagAsk(String userName, int type, int page, int size, String tag) {
        return updateCahe(remote.getTagAsk(userName, type, page, size, tag));
    }

    public List<AskMe> getCached() {
        Iterator<AskMe> iterator = cache.values().iterator();
        List<AskMe> list = new ArrayList<>(cache.size());
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Collections.sort(list, new Comparator<AskMe>() {
            @Override
            public int compare(AskMe lhs, AskMe rhs) {
                return lhs.getDate() == rhs.getDate() ? 0 : lhs.getDate() > rhs.getDate() ? -1 : 1;
            }
        });
        return list;
    }

    public List<BannerItem> getBannerFromCache() {
        return local.getBanner();
    }

    @Override
    public List<AskMe> getLikedList(String userName, int page) {
        List<AskMe> results = local.getLikedList(userName, page);
        if (results == null || results.isEmpty())
            results = getLikedListFromRemote(userName, page);
        return results;
    }

    public List<AskMe> updateCahe(List<AskMe> results) {
        if (results != null) {
            for (AskMe result : results) {
                cache.put(result.getObjectId(), result);
            }

        }
        return results;
    }

    public void updateDB() {
        local.saveOrUpdateAll(cache.values());
        Log.d("realm", "cache updated");
    }

    public AskMe updateCache(AskMe askMe) {
        if (askMe == null)
            return askMe;
        cache.put(askMe.getObjectId(), askMe);
        return cache.get(askMe.getObjectId());
    }

    public List<AskMe> getLikedListFromRemote(String userName, int page) {
        List<AskMe> results = remote.getLikedList(userName, page);
        for (AskMe askMe : results) {
            askMe.setLiked(1);
        }
        updateCahe(results);
        return results;
    }

    public String getOrder(String openId, String accessToken, int expirseIn, String objectid, String userName) {
        return remote.getOrder(openId, accessToken, expirseIn, objectid, userName);
    }

    public List<AskMe> getAllFromLocal() {
        List<AskMe> results = local.getAllAsk(0, 0, 0);
        for (AskMe result : results) {
            cache.put(result.getObjectId(), result);
        }
        return getCached();
    }

    public List<AskMe> getAllFromRemote(int type, int page, int size) {
        List<AskMe> results = remote.getAllAsk(type, page, size);
        updateCahe(results);
        return results;
    }

    @Override
    public boolean like(String usrName, String objectId) {
        if (remote.like(usrName, objectId)) {
            AskMe askMe = cache.get(objectId);
            askMe.setLikeNum(askMe.getLikeNum() + 1);
            askMe.setLiked(1);
            return true;
        }
        return false;
    }

    @Override
    public boolean dislike(String usrName, String objectId) {
        if (remote.dislike(usrName, objectId)) {
            AskMe askMe = cache.get(objectId);
            askMe.setLikeNum(askMe.getLikeNum() - 1);
            askMe.setLiked(0);
            return true;
        }
        return false;
    }

    @Override
    public boolean sendAsk(AskMe askMe, String userName) {
        return remote.sendAsk(askMe, userName);
    }

    @Override
    public AskMe getAskDetail(String userName, String objectId) {
        AskMe askMe = remote.getAskDetail(userName, objectId);
        return updateCache(askMe == null ? getAskFromCache(objectId) : askMe);
    }

    @Override
    public List<AskMe> getMyAskList(String userName, int page) {
        return updateCahe(remote.getMyAskList(userName, page));
    }

    @Override
    public List<AskMe> getHavedList(String userName, int page) {
        return updateCahe(remote.getHavedList(userName, page));
    }

    @Override
    public boolean editAsk(AskMe askMe, String userName) {
        updateCache(askMe);
        return remote.editAsk(askMe, userName);
    }

    @Override
    public boolean deleteAsk(AskMe askMe, String reason, String userName) {
        cache.remove(askMe.getObjectId());
        local.deleteAsk(askMe, reason, userName);
        return remote.deleteAsk(askMe, reason, userName);
    }

    @Override
    public List<BannerItem> getBanner() {
        List<BannerItem> items = remote.getBanner();
        local.resetBanner(items);
        return items;
    }

    @Override
    public List<BannerItem> getTopic(String userName) {
        return remote.getTopic(userName);
    }

    @Override
    public boolean addLook(String objectId) {
        return remote.addLook(objectId);
    }

    @Override
    public boolean debase(String objectId, String userName, String content) {
        return remote.debase(objectId, userName, content);
    }

    @Override
    public String getAllTag() {
        return remote.getAllTag();
    }

    @Override
    public String getCards(String userName) {
        return remote.getCards(userName);
    }

    @Override
    public String getHostAddress() {
        return remote.getHostAddress();
    }

    @Override
    public String getKeyWords(String keyword) {
        return remote.getKeyWords(keyword);
    }

    @Override
    public List<AskMe> searchAsk(String userName, int page, int size, String keyword) {
        return updateCahe(remote.searchAsk(userName, page, size, keyword));
    }

    @Override
    public boolean addCardDownNum(String cardId) {
        return remote.addCardDownNum(cardId);
    }


    public AskMe getAskFromCache(String objectId) {
        return cache.get(objectId);
    }

    public static void saveAskData(Context context, AskMe askMe) {
        SharedPreferences preferences = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("contentSaved", true).commit();
        Gson gson = new Gson();
        String json = gson.toJson(askMe);
        String askDataPath = context.getFilesDir().getAbsolutePath() + "/ask.json";
        File askData = new File(askDataPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(askData, false);
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

    public static void removeData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("contentSaved", false).commit();
        String askDataPath = context.getFilesDir().getAbsolutePath() + "/ask.json";
        File askData = new File(askDataPath);
        if (askData.exists())
            askData.delete();
    }

    public static AskMe readAsk(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("record", Context.MODE_PRIVATE);
        boolean saved = preferences.getBoolean("contentSaved", false);
        if (!saved)
            return null;
        Gson gson = new Gson();
        String askDataPath = context.getFilesDir().getAbsolutePath() + "/ask.json";
        File askData = new File(askDataPath);
        if (!askData.exists())
            return null;
        try {
            Reader reader = new FileReader(askData);
            return gson.fromJson(reader, AskMe.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AskMe getAskMe(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ApiTypeAdapterFactory("askDetail", "data"))
                .create();
        return gson.fromJson(json, AskMe.class);
    }
}
