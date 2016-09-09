package com.myworld.wenwo.data;

import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.BannerItem;

import java.util.List;

/**
 * Created by jianglei on 16/7/11.
 */

public interface AskMeDataSource {
    public AskMe getAsk(String objectId);

    public List<AskMe> getAllAsk(int type, int page, int size);

    public List<AskMe> getTagAsk(String userName, int type, int page, int size, String tag);

    public List<AskMe> getLikedList(String userName, int page);

    public boolean like(String usrName, String objectId);

    public boolean dislike(String usrName, String objectId);

    public boolean sendAsk(AskMe askMe, String userName);

    public AskMe getAskDetail(String userName, String objectId);

    public List<AskMe> getMyAskList(String userName, int page);

    public List<AskMe> getHavedList(String userName, int page);

    public boolean editAsk(AskMe askMe, String userName);

    public boolean deleteAsk(AskMe askMe, String reason, String userName);

    public List<BannerItem> getBanner();

    public List<BannerItem> getTopic(String userName);

    public boolean addLook(String objectId);

    public boolean debase(String objectId, String userName, String content);

    public String getAllTag();

    public String getCards(String userName);

    public String getHostAddress();

    public String getKeyWords(String keyword);

    public List<AskMe> searchAsk(String userName, int page, int size, String keyword);

    public boolean addCardDownNum(String cardId);
}
