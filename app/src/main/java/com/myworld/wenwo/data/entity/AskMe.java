package com.myworld.wenwo.data.entity;

import com.myworld.annotation.CopyObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jianglei on 16/7/11.
 */
@CopyObject
public class AskMe extends RealmObject {

    /**
     * askLevel : 0
     * askTag : {"__type":"Relation","className":"Tag"}
     * askContentShow : 在都江堰南桥远远地就能看到有穿着孙悟空、喜洋洋、米老鼠等玩偶服的人在拉人合影，千万要注意，当你走过去的时候他们可能会突然把你拉住合影，如果你的小伙伴拿出手机或相机拍照，等拍完照之后穿玩偶服的人就拿出证件—“聋哑人拍照一人10元”。虽然不多，但还是感觉被坑得不值啊！
     * buyNum : 1
     * createByName : wenwo
     * askTagStr : [{"tag_name":"玩偶拍照"}]
     * like : {"__type":"Relation","className":"Like"}
     * askIsFree : 1
     * GeoX : 103.615989
     * askType : 6
     * refundInfo : {"__type":"Relation","className":"RefundInfo"}
     * staus : 1
     * askPrice : 0
     * createByUrl : http://ac-m5gmliz1.clouddn.com/3cf42d612ad0ce2d.png
     * likeNum : 20
     * askContentHide :
     * askReason : 我用心做，你放心看
     * haved : {"__type":"Relation","className":"Haved"}
     * askPosition : {"province":"四川省","city":"成都市","district":"都江堰市","township":"幸福镇","detail":"新堰坎"}
     * GeoY : 30.996296
     * createBy : wenwo
     * objectId : 575a2e385bbb500053ccce29
     * createdAt : 2016-06-10T03:04:24.774Z
     * updatedAt : 2016-06-14T01:45:13.231Z
     */

    private String askLevel;
    private String askContentShow;
    private int buyNum;
    private String createByName;
    private String askTagStr;
    private boolean askIsFree;
    private double GeoX;
    private int askType;
    private int staus;
    private float askPrice;
    private String createByUrl;
    private int likeNum;
    private String askContentHide;
    private String askReason;
    private String askPosition;
    private double GeoY;
    private String createBy;
    @PrimaryKey
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private String askImage;
    private String shopName;
    private int liked;
    private boolean isHaved;
    private String askDefault;
    private int score;
    private int lookNum;

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAskDefault() {
        return askDefault;
    }

    public void setAskDefault(String askDefault) {
        this.askDefault = askDefault;
    }

    public boolean isHaved() {
        return isHaved;
    }

    public void setHaved(boolean haved) {
        isHaved = haved;
    }

    public String getAskImage() {
        return askImage;
    }

    public void setAskImage(String askImage) {
        this.askImage = askImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAskLevel() {
        return askLevel;
    }

    public void setAskLevel(String askLevel) {
        this.askLevel = askLevel;
    }

    public String getAskContentShow() {
        return askContentShow;
    }

    public void setAskContentShow(String askContentShow) {
        this.askContentShow = askContentShow;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getAskTagStr() {
        return askTagStr;
    }

    public void setAskTagStr(String askTagStr) {
        this.askTagStr = askTagStr;
    }

    public boolean isAskIsFree() {
        return askIsFree;
    }

    public void setAskIsFree(boolean askIsFree) {
        this.askIsFree = askIsFree;
    }

    public double getGeoX() {
        return GeoX;
    }

    public void setGeoX(double GeoX) {
        this.GeoX = GeoX;
    }

    public int getAskType() {
        return askType;
    }

    public void setAskType(int askType) {
        this.askType = askType;
    }

    public int getStaus() {
        return staus;
    }

    public void setStaus(int staus) {
        this.staus = staus;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }

    public String getCreateByUrl() {
        return createByUrl;
    }

    public void setCreateByUrl(String createByUrl) {
        this.createByUrl = createByUrl;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getAskContentHide() {
        return askContentHide;
    }

    public void setAskContentHide(String askContentHide) {
        this.askContentHide = askContentHide;
    }

    public String getAskReason() {
        return askReason;
    }

    public void setAskReason(String askReason) {
        this.askReason = askReason;
    }

    public String getAskPosition() {
        return askPosition;
    }

    public void setAskPosition(String askPosition) {
        this.askPosition = askPosition;
    }

    public double getGeoY() {
        return GeoY;
    }

    public void setGeoY(double GeoY) {
        this.GeoY = GeoY;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public long getDate() {
        String dateStr = createdAt.split("T")[0].replace('-', '/');
        Date date = new Date(dateStr);
        return date.getTime();
    }

    @Override
    public String toString() {
        return "AskMe{" +
                "askLevel='" + askLevel + '\'' +
                ", askContentShow='" + askContentShow + '\'' +
                ", buyNum=" + buyNum +
                ", createByName='" + createByName + '\'' +
                ", askTagStr='" + askTagStr + '\'' +
                ", askIsFree=" + askIsFree +
                ", GeoX=" + GeoX +
                ", askType=" + askType +
                ", staus=" + staus +
                ", askPrice=" + askPrice +
                ", createByUrl='" + createByUrl + '\'' +
                ", likeNum=" + likeNum +
                ", askContentHide='" + askContentHide + '\'' +
                ", askReason='" + askReason + '\'' +
                ", askPosition='" + askPosition + '\'' +
                ", GeoY=" + GeoY +
                ", createBy='" + createBy + '\'' +
                ", objectId='" + objectId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", askImage='" + askImage + '\'' +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
