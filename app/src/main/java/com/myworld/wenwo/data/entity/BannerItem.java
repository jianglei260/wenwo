package com.myworld.wenwo.data.entity;

import com.myworld.annotation.CopyObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jianglei on 16/8/10.
 */
@CopyObject
public class BannerItem extends RealmObject{
    private String carouselClickURL;
    private String carouselImage;
    private String carouselName;
    @PrimaryKey
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCarouselClickURL() {
        return carouselClickURL;
    }

    public void setCarouselClickURL(String carouselClickURL) {
        this.carouselClickURL = carouselClickURL;
    }

    public String getCarouselName() {
        return carouselName;
    }

    public void setCarouselName(String carouseName) {
        this.carouselName = carouseName;
    }

    public String getCarouselImage() {
        return carouselImage;
    }

    public void setCarouselImage(String carouselImage) {
        this.carouselImage = carouselImage;
    }
}
