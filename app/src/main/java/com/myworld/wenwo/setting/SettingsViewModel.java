package com.myworld.wenwo.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.update.AppUpdateTool;
import com.myworld.wenwo.update.VersionInfo;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/23.
 */

public class SettingsViewModel implements ViewModel {
    private Context context;
    public ReplyCommand updateClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            final ProgressDialog progressDialog=new ProgressDialog(context);
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
            });
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
    }
}
