package com.myworld.wenwo.view.widget;

/**
 * Created by jianglei on 16/7/19.
 */

public class IndicatorItem {
    private int imageRes;
    private String title;
    private int color;

    public IndicatorItem(int imageRes, String title, int color) {
        this.imageRes = imageRes;
        this.title = title;
        this.color = color;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
