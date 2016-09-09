package com.myworld.wenwo.data.source.local;

import com.baidu.platform.comapi.map.A;
import com.myworld.wenwo.application.WenWoApplication;
import com.myworld.wenwo.data.AskMeDataSource;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.BannerItem;
import com.myworld.wenwo.utils.ObjectCopyUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jianglei on 16/7/11.
 */

public class AskLocalDataSource implements AskMeDataSource {
    private static AskLocalDataSource local;

    public static AskLocalDataSource getInstance() {
        if (local == null)
            local = new AskLocalDataSource();
        return local;
    }

    public AskLocalDataSource() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(WenWoApplication.getInstance()).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Override
    public AskMe getAsk(String objectId) {
        return ObjectCopyUtil.copyAskMe(Realm.getDefaultInstance().where(AskMe.class).equalTo("objectId", objectId).findFirst());
    }

    @Override
    public List<AskMe> getAllAsk(int type, int page, int size) {
        List<AskMe> results = Realm.getDefaultInstance().where(AskMe.class).findAll();
        return copyResult(results);
    }

    @Override
    public List<AskMe> getTagAsk(String userName, int type, int page, int size, String tag) {
        return null;
    }

    public static List<AskMe> copyResult(List<AskMe> source) {
        List<AskMe> outResults = new ArrayList<>(source.size());
        for (AskMe askMe : source) {
            outResults.add(ObjectCopyUtil.copyAskMe(askMe));
        }
        return outResults;
    }

    @Override
    public List<AskMe> getLikedList(String userName, int page) {
        List<AskMe> results = Realm.getDefaultInstance().where(AskMe.class).equalTo("liked", true).findAll();
        return copyResult(results);
    }

    @Override
    public boolean like(String usrName, String objectId) {
        Realm realm = Realm.getDefaultInstance();
        AskMe askMe = realm.where(AskMe.class).equalTo("objectId", objectId).findFirst();
        if (askMe == null)
            return false;
        askMe.setLiked(1);
        saveOrUpdate(askMe);
        return true;
    }

    @Override
    public boolean dislike(String usrName, String objectId) {
        Realm realm = Realm.getDefaultInstance();
        AskMe askMe = realm.where(AskMe.class).equalTo("objectId", objectId).findFirst();
        if (askMe == null)
            return false;
        askMe.setLiked(0);
        realm.beginTransaction();
        realm.insertOrUpdate(askMe);
        realm.commitTransaction();
        return true;
    }

    public void saveOrUpdate(AskMe askMe) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(askMe);
        realm.commitTransaction();
    }

    public void saveOrUpdateAll(Collection<AskMe> askMes) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(askMes);
        realm.commitTransaction();
    }

    @Override
    public boolean sendAsk(AskMe askMe, String userName) {
        saveOrUpdate(askMe);
        return true;
    }


    @Override
    public AskMe getAskDetail(String userName, String objectId) {
        return null;
    }

    @Override
    public List<AskMe> getMyAskList(String userName, int page) {
        return null;//// TODO: 16/7/30
    }

    @Override
    public List<AskMe> getHavedList(String userName, int page) {
        return null;//// TODO: 16/7/30  
    }

    @Override
    public boolean editAsk(AskMe askMe, String userName) {
        return true;
    }

    @Override
    public boolean deleteAsk(AskMe askMe, String reason, String userName) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        AskMe localAsk = realm.where(AskMe.class).equalTo("objectId", askMe.getObjectId()).findFirst();
        if (localAsk != null) {
            localAsk.deleteFromRealm();
        }
        realm.commitTransaction();
        return true;
    }

    @Override
    public List<BannerItem> getBanner() {
        List<BannerItem> items = new ArrayList<>();
        List<BannerItem> results = Realm.getDefaultInstance().where(BannerItem.class).findAll();
        for (BannerItem item : results) {
            items.add(ObjectCopyUtil.copyBannerItem(item));
        }
        return items;
    }
    public void resetBanner(List<BannerItem> bannerItems){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(BannerItem.class);
        realm.insert(bannerItems);
        realm.commitTransaction();
    }

    @Override
    public List<BannerItem> getTopic(String userName) {
        return null;
    }

    @Override
    public boolean addLook(String objectId) {
        return false;
    }

    @Override
    public boolean debase(String objectId, String userName, String content) {
        return false;
    }

    @Override
    public String getAllTag() {
        return "";
    }

    @Override
    public String getCards(String userName) {
        return null;
    }

    @Override
    public String getHostAddress() {
        return null;
    }

    @Override
    public String getKeyWords(String keyword) {
        return null;
    }

    @Override
    public List<AskMe> searchAsk(String userName, int page, int size, String keyword) {
        return null;
    }

    @Override
    public boolean addCardDownNum(String cardId) {
        return false;
    }


}
