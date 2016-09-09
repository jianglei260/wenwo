package com.myworld.wenwo.data.entity;

/**
 * Created by jianglei on 16/7/30.
 */

public class UserInfo {
    private int askListCount;
    private int buyListCount;
    private int foodLikeListCount;
    private float totalIncome;
    private String userShowName;
    private String userHead;
    public int getAskListCount() {
        return askListCount;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public void setAskListCount(int askListCount) {
        this.askListCount = askListCount;
    }

    public int getBuyListCount() {
        return buyListCount;
    }

    public void setBuyListCount(int buyListCount) {
        this.buyListCount = buyListCount;
    }

    public int getFoodLikeListCount() {
        return foodLikeListCount;
    }

    public void setFoodLikeListCount(int foodLikeListCount) {
        this.foodLikeListCount = foodLikeListCount;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getUserShowName() {
        return userShowName;
    }

    public void setUserShowName(String userShowName) {
        this.userShowName = userShowName;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "askListCount=" + askListCount +
                ", buyListCount=" + buyListCount +
                ", foodLikeListCount=" + foodLikeListCount +
                ", totalIncome=" + totalIncome +
                ", userShowName='" + userShowName + '\'' +
                ", userHead='" + userHead + '\'' +
                '}';
    }
}
