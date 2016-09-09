package com.myworld.wenwo.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/8/15.
 */

public class CheckForLogin {
    public static void needLogin(final Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(R.string.request_login_title).setMessage(R.string.request_login_message)
                .setPositiveButton(R.string.request_login_agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }).setNegativeButton(R.string.request_login_disagree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
