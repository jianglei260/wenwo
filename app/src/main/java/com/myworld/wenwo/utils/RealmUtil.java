package com.myworld.wenwo.utils;

import java.lang.reflect.Field;

/**
 * Created by jianglei on 16/7/30.
 */

public class RealmUtil {
    public static void ensureMainThread(long mainId){
        try {
            Field id = Thread.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(Thread.currentThread(), mainId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
