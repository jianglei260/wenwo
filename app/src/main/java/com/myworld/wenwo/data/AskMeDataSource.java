package com.myworld.wenwo.data;

import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.BannerItem;

import java.util.List;

/**
 * Created by jianglei on 16/7/11.
 */

public interface AskMeDataSource {
    AskMe getAsk(String objectId);

    List<AskMe> getAllAsk(int type, int page, int size);

    List<AskMe> getTagAsk(String userName, int type, int page, int size, String tag);

    List<AskMe> getLikedList(String userName, int page);

    boolean like(String usrName, String objectId);

    boolean dislike(String usrName, String objectId);

    boolean sendAsk(AskMe askMe, String userName);

    boolean autoSendAsk(AskMe askMe, String userName);

    AskMe getAskDetail(String userName, String objectId);

    List<AskMe> getMyAskList(String userName, int page);

    List<AskMe> getHavedList(String userName, int page);

    boolean editAsk(AskMe askMe, String userName);

    boolean deleteAsk(AskMe askMe, String reason, String userName);

    List<BannerItem> getBanner();

    List<BannerItem> getTopic(String userName);

    boolean addLook(String objectId);

    boolean debase(String objectId, String userName, String content);

    String getAllTag();

    String getCards(String userName);

    String getHostAddress();

    String getKeyWords(String keyword);

    List<AskMe> searchAsk(String userName, int page, int size, String keyword);

    boolean addCardDownNum(String cardId);

    boolean addLookUser(String userId);
}
