package com.myworld.wenwo.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatImageView;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.myworld.wenwo.data.repository.UserRepository;

import im.fir.sdk.FIR;

/**
 * Created by jianglei on 16/7/16.
 */

public class WenWoApplication extends Application {
    private static WenWoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
//        FreelineCore.init(this);
        instance = this;
//        ImagePipelineConfig pipelineConfig = ImagePipelineConfig.newBuilder(this).set.build();
        Fresco.initialize(this);

//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Config.USERNAME = UserRepository.getInstance().getUserFromLocal(this);
        FIR.init(this);
    }

    public static WenWoApplication getInstance() {
        return instance;
    }
    //    @Override
//    public Resources getResources() {
//        WenWoResource resource=new WenWoResource(this,getAssets(),super.getResources().getDisplayMetrics(),super.getResources().getConfiguration());
//        return resource;
//    }
}
