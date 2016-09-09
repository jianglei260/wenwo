package com.myworld.wenwo.bus;

import com.kelin.mvvmlight.messenger.WeakAction;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by jianglei on 16/7/30.
 */

public class EventBus<T> {
    private static EventBus instance;
    private List<WeakReference<T>> cachedObject = new ArrayList<>();

    public static EventBus getInstance() {
        if (instance == null)
            instance = new EventBus();
        return instance;
    }

    public void regist(T object) {
        WeakReference<T> weakReference = new WeakReference<T>(object);
        cachedObject.add(weakReference);
    }

    public void unRegist(T object) {
        for (WeakReference<T> reference : cachedObject) {
            if (reference.get().equals(object))
                cachedObject.remove(reference);
        }
    }

    public void onEevent(Class<T> clazz, String method, Object... params) {
        for (WeakReference<T> entry : cachedObject) {
            T object = entry.get();
            if (object == null)
                continue;
            if (object.getClass().getName().equals(clazz.getName())) {
                try {
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method child : methods) {
                        OnEvent onEvent = child.getAnnotation(OnEvent.class);
                        if (onEvent == null)
                            continue;
                        if (onEvent.value().equals(method)) {
                            child.setAccessible(true);
                            child.invoke(object, params);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
