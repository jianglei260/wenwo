package com.myworld.wenwo.login;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mob.commons.SHARESDK;
import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityLoginBinding;
import com.myworld.wenwo.databinding.ActivityLoginWechatBinding;
import com.myworld.wenwo.view.widget.TitleBar;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.sharesdk.framework.ShareSDK;

public class LoginActivity extends AppCompatActivity {

    private Matrix matrix;
    float centerX, centerY;
    private ValueAnimator animator;
    private float xOffset, yOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        ActivityLoginWechatBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login_wechat);
        binding.setLoginViewModel(new LoginViewModel(this));
        ImageView imageView = (ImageView) findViewById(R.id.login_bg);

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        startAnimation(imageView);
    }


    private void startAnimation(final ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        int width = drawable.getBitmap().getWidth();
        int height = drawable.getBitmap().getHeight();
        centerX = getResources().getDisplayMetrics().widthPixels / 2;
        centerY = getResources().getDisplayMetrics().heightPixels / 2;
        xOffset = width / 2 - centerX;
        yOffset = height / 2 - centerY;
        Matrix matrix = new Matrix(imageView.getImageMatrix());
        matrix.postTranslate(-xOffset, -yOffset);
        imageView.setImageMatrix(matrix);
        imageView.invalidate();
        animator = new ValueAnimator();
        animator.setFloatValues(0, 60);
        animator.setDuration(60000);
        animator.setRepeatCount(10);
        animator.setStartDelay(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float degree = 0.0f;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Matrix matrix = new Matrix(imageView.getImageMatrix());
                if (degree > 100) {
                    matrix.postRotate(0.02f, centerX, centerY);
//                    matrix.postScale(0.6f, 0.6f);
                } else {
                    matrix.postRotate(-0.02f, centerX, centerY);
                }
                degree += 0.05f;
//                Log.d("degree", degree + "");
                if (degree > 200)
                    degree = 0;
                imageView.setImageMatrix(matrix);
                imageView.invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        if (animator.isRunning())
            animator.cancel();
        super.onDestroy();
    }
}
