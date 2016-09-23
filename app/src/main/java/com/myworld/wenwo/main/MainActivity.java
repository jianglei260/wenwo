package com.myworld.wenwo.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.facebook.drawee.view.SimpleDraweeView;
import com.myworld.wenwo.Main2Activity;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.AddActivity;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.application.WenWoResource;
import com.myworld.wenwo.asksearch.AskSearchActivity;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.common.BaseActivity;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.data.source.remote.AskMeService;
import com.myworld.wenwo.data.source.remote.RetrofitProvider;
import com.myworld.wenwo.found.FoundFragment;
import com.myworld.wenwo.like.LikeFragment;
import com.myworld.wenwo.login.CheckForLogin;
import com.myworld.wenwo.user.UserActivity;
import com.myworld.wenwo.utils.ViewServer;
import com.myworld.wenwo.view.widget.TitleBar;
import com.myworld.wenwo.view.widget.WenwoDialog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {
    private FoundFragment foundFragment;
    private LikeFragment likeFragment;
    private TitleBar titleBar;
    private BottomNavigationBar bottomNavigationBar;
    private ViewServer viewServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottomNavigationBar.setBarBackgroundColor(R.color.bar_background_color);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_found_selected, "发现").setInactiveIconResource(R.drawable.ic_found).setActiveColorResource(R.color.colorPrimary).setInActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_like_selected, "喜欢").setInactiveIconResource(R.drawable.ic_like).setActiveColorResource(R.color.colorPrimary).setInActiveColorResource(R.color.colorPrimary))
                .initialise();
        initBottomBar(bottomNavigationBar);
        bottomNavigationBar.setTabSelectedListener(this);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            initTitleBar(titleBar);
        }

        foundFragment = FoundFragment.newInstance();
        likeFragment = LikeFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container, likeFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.container, foundFragment).commit();
        EventBus.getInstance().regist(this);
        viewServer = ViewServer.get(this);
        viewServer.addWindow(this);
        try {
            viewServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
    }

    public void addDoubleClickListener(GestureDetector.OnDoubleTapListener listener) {
        titleBar.addDoubleClickListener(listener);
    }

    private static final String TAG = "MainActivity";

    private void initTitleBar(TitleBar titleBar) {
        titleBar.getTextView().setTextSize(20);
        titleBar.getTextView().setText(getString(R.string.app_name));
        titleBar.getTextView().setTextColor(getResources().getColor(R.color.colorPrimary));
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
        titleBar.getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AskSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initBottomBar(BottomNavigationBar navigationBar) {
        try {
            Field field = BottomNavigationBar.class.getDeclaredField("mTabContainer");
            field.setAccessible(true);
            LinearLayout tabContainer = (LinearLayout) field.get(navigationBar);
            ImageView addImageView = new ImageView(this);
            addImageView.setImageResource(R.drawable.ic_add);
            int padding = getResources().getDimensionPixelSize(R.dimen.badge_top_margin);
            addImageView.setPadding(padding, 0, padding, padding);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            tabContainer.setPadding(0, getResources().getDimensionPixelSize(R.dimen.dp_6), 0, 0);
            tabContainer.addView(addImageView, 1, params);
            addImageView.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTabSelected(int position) {
        if (position == 0) {
            if (foundFragment != null) {
                getFragmentManager().beginTransaction().hide(likeFragment).commit();
                getFragmentManager().beginTransaction().show(foundFragment).commit();
            }

        } else if (position == 1) {
            if (likeFragment != null) {
                getFragmentManager().beginTransaction().hide(foundFragment).commit();
                getFragmentManager().beginTransaction().show(likeFragment).commit();
//                likeFragment.refreshData();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @OnEvent("selectTab")
    public void selectTab(int tab) {
        bottomNavigationBar.selectTab(tab);
    }

    @Override
    protected void onDestroy() {
//        viewServer.stop();
        super.onDestroy();
        AskMeRepository.getInstance().updateDB();
        Log.d("mainActivity", "onDestory");
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(Config.USERNAME)) {
            CheckForLogin.needLogin(this);
            return;
        }
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.add_open_anim, R.anim.alpha_out_anim);
    }

}
