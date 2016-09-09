package com.myworld.wenwo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.myworld.wenwo.common.BaseActivity;

import java.util.WeakHashMap;

/**
 * Created by jianglei on 16/8/15.
 */

public class ActivityTool {
    private static WeakHashMap<Class<? extends BaseActivity>, Object> objects = new WeakHashMap<>();

    public static void startActivity(Context context, Class<? extends BaseActivity> activity, Object object) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        objects.put(activity, object);
    }

    public static void startActivity(Context context, Class<? extends BaseActivity> activity, Intent intent, Object object) {
        intent.setClass(context, activity);
        context.startActivity(intent);
        objects.put(activity, object);
    }

    public static <T> void onActivityCreated(BaseActivity<T> activity) {
        T o = (T) objects.get(activity.getClass());
        if (o instanceof CallBack)
            ((CallBack) o).onCreated(activity);
        if (o != null) {
            activity.onObjectSet(o);
        }
    }

    public static  <T extends BaseActivity> void startActivityForCallBack(Context context, Class<T> activity, CallBack<T> callBack) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        objects.put(activity, callBack);
    }

    public interface CallBack<T> {
        public void onCreated(T activity);
    }
}
