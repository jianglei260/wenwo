package com.myworld.wenwo.application;

import android.databinding.ObservableField;
import android.os.Environment;

import java.io.File;

/**
 * Created by jianglei on 16/7/28.
 */

public class Config {
    public static String USERNAME = "";
    public static int PAGE_SIZE = 20;
    public static final String APP_ID = "wxb4b71b749fb0ea09";
    public static double geo_latitude = 30;
    public static double geo_longtitude = 103;
    public static int range = 2000;
    public static ObservableField<String> geo_address = new ObservableField<>("");
    public static double mark_geo_x;
    public static double mark_geo_y;
    public static final File EXTERNAL_DIR = Environment.getExternalStoragePublicDirectory("/wenwo");
    public static boolean like_state_changed = true;
}
