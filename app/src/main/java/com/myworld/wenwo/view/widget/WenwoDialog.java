package com.myworld.wenwo.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/7/29.
 */

public class WenwoDialog {
    private AlertDialog dialog;
    private Context context;

    public WenwoDialog(Context context) {
        this.context = context;
    }

    public void show() {
        if (dialog == null)
            dialog = new AlertDialog.Builder(context, R.style.dialog).setView(R.layout.dialog_lodding).create();
        dialog.show();
    }

    public void show(String message) {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context, R.style.dialog).setView(R.layout.dialog_lodding).show();
            LoaddingView loaddingView = (LoaddingView) dialog.findViewById(R.id.loading_view);
            loaddingView.setTitleText(message);
        }
    }

    public AlertDialog getDialog() {
        if (dialog == null)
            dialog = new AlertDialog.Builder(context, R.style.dialog).setView(R.layout.dialog_lodding).create();
        return dialog;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
