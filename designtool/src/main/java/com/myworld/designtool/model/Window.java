package com.myworld.designtool.model;

/**
 * Created by jianglei on 16/9/10.
 */

public class Window {
    private final String mTitle;
    private final int mHashCode;

    public Window(String mTitle, int mHashCode) {
        this.mTitle = mTitle;
        this.mHashCode = mHashCode;
    }
    public static Window getFocusedWindow()
    {
        return new Window("<Focused Window>", -1);
    }
    public String getmTitle() {
        return mTitle;
    }

    public int getmHashCode() {
        return mHashCode;
    }

    public String encode() {
        return Integer.toHexString(this.mHashCode);
    }
}
