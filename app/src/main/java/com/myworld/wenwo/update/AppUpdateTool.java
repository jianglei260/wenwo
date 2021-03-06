package com.myworld.wenwo.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jianglei on 16/8/4.
 */

public class AppUpdateTool {
    private String requestUrl;
    private boolean autoUpdate;
    private long checkTime;
    private Context context;
    private long reference;


    public static class Builder {
        private AppUpdateTool updateTool;

        public Builder(Context context) {
            updateTool = new AppUpdateTool();
            updateTool.context = context;
        }

        public Builder setRequestUrl(String url) {
            updateTool.requestUrl = url;
            return this;
        }

        public Builder setAutoUpdate(boolean auto) {
            updateTool.autoUpdate = auto;
            return this;
        }

        public Builder setCheckTime(long time) {
            updateTool.checkTime = time;
            return this;
        }

        public AppUpdateTool build() {
            return updateTool;
        }
    }

    public void downloadComplete(Uri fileUri, VersionInfo versionInfo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void checkUpdate() {
        long lastTime = getLastTime();
        long now = System.currentTimeMillis();
        if (now - lastTime > getCheckTime())
            checkUpdate(null);
    }

    public void checkUpdate(final UpdateCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(requestUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (callback != null)
                    callback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                VersionInfo versionInfo = new VersionInfo();
                try {
                    JSONObject rootObject = new JSONObject(result);
                    JSONObject object = rootObject.getJSONObject("data");
                    versionInfo.setFeature(object.getString("feature"));
                    versionInfo.setUrl(object.getString("url"));
                    versionInfo.setVersionCode(object.getInt("versionCode"));
                    versionInfo.setVersionName(object.getString("versionName"));
                    saveCheckInfo();
                    if (callback != null)
                        callback.needUpdate(versionInfo.getVersionCode() > getCurrentVersion(), versionInfo);
                    else
                        autoUpdate(versionInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void autoUpdate(VersionInfo versionInfo) {
        if (versionInfo.getVersionCode() > getCurrentVersion())
            doUpdate(versionInfo);
    }

    private void saveCheckInfo() {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        preferences.edit().putLong("time", System.currentTimeMillis()).commit();
        if (checkTime > 0)
            preferences.edit().putLong("checkTime", checkTime).commit();
    }

    private void saveUpdateInfo(long id, VersionInfo versionInfo) {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        preferences.edit().putLong("id", id).commit();
        preferences.edit().putString("versionName", versionInfo.getVersionName()).commit();
        preferences.edit().putString("feature", versionInfo.getFeature()).commit();
        preferences.edit().putString("url", versionInfo.getUrl()).commit();
        preferences.edit().putInt("versionCode", versionInfo.getVersionCode()).commit();
    }

    public long getDownloadId() {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        return preferences.getLong("id", 0);
    }

    public long getCheckTime() {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        return preferences.getLong("checkTime", 0);
    }

    public long getLastTime() {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        return preferences.getLong("time", 0);
    }

    public VersionInfo getVersionInfo() {
        SharedPreferences preferences = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        String name = preferences.getString("versionName", "");
        String feature = preferences.getString("feature", "");
        String url = preferences.getString("url", "");
        int code = preferences.getInt("versionCode", 0);
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setVersionCode(code);
        versionInfo.setVersionName(name);
        versionInfo.setUrl(url);
        versionInfo.setFeature(feature);
        return versionInfo;
    }

    public int getCurrentVersion() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private File checkFile(VersionInfo versionInfo) {
        String path = Environment.getExternalStoragePublicDirectory("/wenwo").getAbsolutePath() + "/wenwo" + versionInfo.getVersionName() + ".apk";
        return new File(path);
    }

    public void doUpdate(final VersionInfo versionInfo) {
        File file = checkFile(versionInfo);
        if (file.exists()) {
            downloadComplete(Uri.fromFile(file), versionInfo);
            return;
        }
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionInfo.getUrl()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir("/wenwo", "/wenwo" + versionInfo.getVersionName() + ".apk");
        reference = downloadManager.enqueue(request);
        saveUpdateInfo(reference, versionInfo);
    }

    public interface UpdateCallback {
        void needUpdate(boolean needUpdate, VersionInfo versionInfo);

        void onFailure();
    }
}
