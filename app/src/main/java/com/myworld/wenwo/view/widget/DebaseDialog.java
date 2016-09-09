package com.myworld.wenwo.view.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.myworld.wenwo.R;

/**
 * Created by jianglei on 16/8/16.
 */

public class DebaseDialog extends AlertDialog {
    private EditText editText;
    private TextView label1, label2, send;
    private int layoutRes;

    public DebaseDialog(Context context) {
        super(context, R.style.debase_dialog);
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_debase);
        editText = (EditText) findViewById(R.id.edit_text);
        label1 = (TextView) findViewById(R.id.debase_label1);
        label2 = (TextView) findViewById(R.id.debase_label2);
        send = (TextView) findViewById(R.id.send);
        label1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(label1.getText());
            }
        });
        label2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(label2.getText());
            }
        });
        findViewById(R.id.dialog_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setSendListener(final SendListener sendListener) {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListener.sendText(editText.getText().toString());
            }
        });
    }

    public interface SendListener {
        public void sendText(String text);
    }
}
