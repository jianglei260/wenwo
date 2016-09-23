package com.myworld.designtool;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.myworld.designtool.client.ViewClient;
import com.myworld.designtool.model.ViewNode;
import com.myworld.designtool.model.Window;
import com.myworld.designtool.view.BorderView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private BorderView borderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        borderView= (BorderView) findViewById(R.id.border);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Window> windows = ViewClient.get().loadWindows();
                for (final Window window : windows) {
                    Log.d("————————window:" + window.getmTitle(), window.encode());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                final ViewNode viewNode = ViewClient.get().dumpView(windows.get(0)).children.get(0);
                final Bitmap bitmap=ViewClient.get().loadCapture(windows.get(0),viewNode.children.get(1));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        borderView.setViewNode(viewNode.children.get(1));
                    }
                });

//                if (windows.size() > 0) {
//                    ViewNode viewNode = ViewClient.get().dumpView(windows.get(0));
//                    List<ViewNode> childrens=viewNode.children;
//                    for (ViewNode children:childrens){
//                        textView.append("___children:"+viewNode.name);
//                    }
//                }
            }
        }).start();
    }
}
