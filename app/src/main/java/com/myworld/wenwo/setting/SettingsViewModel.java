package com.myworld.wenwo.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.update.AppUpdateTool;
import com.myworld.wenwo.update.VersionInfo;
import com.myworld.wenwo.utils.FileUtil;
import com.myworld.wenwo.utils.ObservableUtil;

import java.io.File;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/23.
 */

public class SettingsViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> cacheSize = new ObservableField<>();
    public ObservableField<String> version = new ObservableField<>();
    public ReplyCommand updateClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.checking_update));
            progressDialog.show();
            final AppUpdateTool updateTool = new AppUpdateTool.Builder(context).setAutoUpdate(true).setRequestUrl("http://wenwo.leanapp.cn/manage/version").build();
            updateTool.checkUpdate(new AppUpdateTool.UpdateCallback() {
                @Override
                public void needUpdate(boolean needUpdate, final VersionInfo versionInfo) {
                    progressDialog.dismiss();
                    if (needUpdate)
                        ((SettingsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkUpdate(updateTool, versionInfo);
                            }
                        });
                    else {
                        ((SettingsActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog dialog = new AlertDialog.Builder(context).setTitle("版本更新").setMessage("已是最新版本").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
                        });

                    }
                }

                @Override
                public void onFailure() {
                    try {
                        Toast.makeText(context, context.getString(R.string.checking_update_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    });

    public ReplyCommand cleanCache = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            deleteCache();
        }
    });

    private void checkUpdate(final AppUpdateTool tool, final VersionInfo versionInfo) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("发现新版本").setMessage(versionInfo.getFeature()).setPositiveButton("下载新版本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tool.doUpdate(versionInfo);
                dialog.dismiss();
            }
        }).show();
    }

    public SettingsViewModel(Context context) {
        this.context = context;
        checkCacheSize();
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            version.set(versionName + "版");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteCache() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                FileUtil.deleteFile(context.getCacheDir());
                FileUtil.deleteFile(context.getExternalCacheDir());
                FileUtil.deleteFile(new File(Realm.getDefaultInstance().getPath()));
                subscriber.onNext(true);
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean o) {
                dialog.dismiss();
                checkCacheSize();
            }
        });
    }

    public void checkCacheSize() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long size = 0;
                size = FileUtil.getFileSize(size, context.getCacheDir());
                size = FileUtil.getFileSize(size, context.getExternalCacheDir());
                size = FileUtil.getFileSize(size, new File(Realm.getDefaultInstance().getPath()));
                subscriber.onNext(size);
            }
        }, new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                cacheSize.set(Formatter.formatFileSize(context, aLong));
            }
        });
    }
}
