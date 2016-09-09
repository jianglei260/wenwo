package com.myworld.wenwo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.simple);
        draweeView.setImageURI(Uri.parse("http://wx.qlogo.cn/mmopen/zngt7mjBQUcP31iaAy5ARAd2o4RUWgM4eQsWR8a5p5QoDCEMIj7u3Jn7libpeeiatiaJD9Zu7dXibukPZvFuaYqRfIahicR73S8wtA/"));
    }
}
