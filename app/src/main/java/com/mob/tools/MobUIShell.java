package com.mob.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mob.tools.FakeActivity;
import com.mob.tools.MobLog;
import com.mob.tools.utils.ReflectHelper;

import java.util.HashMap;

/**
 * Created by jianglei on 16/8/8.
 */

public class MobUIShell extends Activity {
    private static HashMap<String, FakeActivity> executors = new HashMap();
    private FakeActivity executor;
    public static int forceTheme;

    public MobUIShell() {
    }

    protected static String registerExecutor(Object executor) {
        String launchTime = String.valueOf(System.currentTimeMillis());
        return registerExecutor(launchTime, executor);
    }

    protected static String registerExecutor(String scheme, Object executor) {
        executors.put(scheme, (FakeActivity) executor);
        return scheme;
    }

    public void setTheme(int resid) {
        if (forceTheme > 0) {
            super.setTheme(forceTheme);
        } else {
            super.setTheme(resid);
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = this.getIntent();
        String launchTime = mIntent.getStringExtra("launch_time");
        String executorName = mIntent.getStringExtra("executor_name");
        this.executor = (FakeActivity) executors.remove(launchTime);
        if (this.executor == null) {
            String uriScheme = mIntent.getScheme();
            this.executor = (FakeActivity) executors.remove(uriScheme);
            if (this.executor == null) {
                this.executor = this.getDefault();
                if (this.executor == null) {
                    MobLog.getInstance().w(new RuntimeException("Executor lost! launchTime = " + launchTime + ", executorName: " + executorName));
                    super.onCreate(savedInstanceState);
                    this.finish();
                    return;
                }
            }
        }

        MobLog.getInstance().i("MobUIShell found executor: " + this.executor.getClass(), new Object[0]);
        this.executor.setActivity(this);
        super.onCreate(savedInstanceState);
        MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onCreate", new Object[0]);
        this.executor.onCreate();
    }

    public FakeActivity getDefault() {
        try {
            ActivityInfo t = this.getPackageManager().getActivityInfo(this.getComponentName(), 128);
            String defaultActivity = t.metaData.getString("defaultActivity");
            if (!TextUtils.isEmpty(defaultActivity)) {
                if (defaultActivity.startsWith(".")) {
                    defaultActivity = this.getPackageName() + defaultActivity;
                }

                String name = ReflectHelper.importClass(defaultActivity);
                if (!TextUtils.isEmpty(name)) {
                    Object fa = ReflectHelper.newInstance(name, new Object[0]);
                    if (fa != null && fa instanceof FakeActivity) {
                        return (FakeActivity) fa;
                    }
                }
            }
        } catch (Throwable var5) {
            MobLog.getInstance().w(var5);
        }

        return null;
    }

    public void setContentView(int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, (ViewGroup) null);
        this.setContentView(contentView);
    }

    public void setContentView(View view) {
        if (view != null) {
            super.setContentView(view);
            if (this.executor != null) {
                this.executor.setContentView(view);
            }

        }
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            if (params == null) {
                super.setContentView(view);
            } else {
                super.setContentView(view, params);
            }

            if (this.executor != null) {
                this.executor.setContentView(view);
            }

        }
    }

    protected void onNewIntent(Intent intent) {
        if (this.executor == null) {
            super.onNewIntent(intent);
        } else {
            this.executor.onNewIntent(intent);
        }

    }

    protected void onStart() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onStart", new Object[0]);
            this.executor.onStart();
        }

        super.onStart();
    }

    protected void onResume() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onResume", new Object[0]);
            this.executor.onResume();
        }

        super.onResume();
    }

    protected void onPause() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onPause", new Object[0]);
            this.executor.onPause();
        }

        super.onPause();
    }

    protected void onStop() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onStop", new Object[0]);
            this.executor.onStop();
        }

        super.onStop();
    }

    protected void onRestart() {
        if (this.executor != null) {
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onRestart", new Object[0]);
            this.executor.onRestart();
        }

        super.onRestart();
    }

    protected void onDestroy() {
        if (this.executor != null) {
            this.executor.sendResult();
            MobLog.getInstance().d(this.executor.getClass().getSimpleName() + " onDestroy", new Object[0]);
            this.executor.onDestroy();
        }

        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.executor != null) {
            this.executor.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = false;
        if (this.executor != null) {
            ret = this.executor.onKeyEvent(keyCode, event);
        }

        return ret ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean ret = false;
        if (this.executor != null) {
            ret = this.executor.onKeyEvent(keyCode, event);
        }

        return ret ? true : super.onKeyUp(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.executor != null) {
            this.executor.onConfigurationChanged(newConfig);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (this.executor != null) {
            this.executor.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void finish() {
        if (this.executor == null || !this.executor.onFinish()) {
            super.finish();
            overridePendingTransition(0, 0);
        }
    }

    public Object getExecutor() {
        return this.executor;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean res = super.onCreateOptionsMenu(menu);
        return this.executor != null ? this.executor.onCreateOptionsMenu(menu) : res;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = super.onOptionsItemSelected(item);
        return this.executor != null ? this.executor.onOptionsItemSelected(item) : res;
    }

    static {
        MobLog.getInstance().d("===============================", new Object[0]);
        String version = "2016-07-07".replace("-0", "-");
        version = version.replace("-", ".");
        MobLog.getInstance().d("MobTools " + version, new Object[0]);
        MobLog.getInstance().d("===============================", new Object[0]);
    }
}
