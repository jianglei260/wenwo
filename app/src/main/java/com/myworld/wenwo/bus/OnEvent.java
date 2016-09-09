package com.myworld.wenwo.bus;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import retrofit2.http.FormUrlEncoded;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jianglei on 16/7/30.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface OnEvent {
    String value();
}
