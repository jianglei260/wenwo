package com.myworld.wenwo.vipcard;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BaseInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.myworld.wenwo.R;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.databinding.LayoutVipcardBackBinding;
import com.myworld.wenwo.databinding.LayoutVipcardFrontBinding;

/**
 * Created by jianglei on 16/9/14.
 */

public class VipCardDialog extends AlertDialog {
    private AskMe askMe;
    private LayoutVipcardBackBinding backBinding;
    private LayoutVipcardFrontBinding frontBinding;

    public VipCardDialog(Context context, AskMe askMe) {
        super(context, R.style.debase_dialog);
        this.askMe = askMe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vipcard);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        VipCardViewModel cardViewModel = new VipCardViewModel(getContext(), askMe);

        frontBinding = DataBindingUtil.inflate(inflater, R.layout.layout_vipcard_front, null, false);
        backBinding = DataBindingUtil.inflate(inflater, R.layout.layout_vipcard_back, null, false);
        frontBinding.setCardViewModel(cardViewModel);
        backBinding.setViewModel(cardViewModel);
        final FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.addView(backBinding.getRoot());
        container.addView(frontBinding.getRoot());
        frontBinding.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRote();
            }
        });
        backBinding.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backRote();
            }
        });
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void backRote() {
        backBinding.cardLayout.setVisibility(View.VISIBLE);
        backBinding.cardLayout.setRotationY(0);
        frontBinding.cardLayout.setRotationY(-90);
        frontBinding.cardLayout.setVisibility(View.INVISIBLE);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
        animator.setInterpolator(new BaseInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                if (value <= 90) {
                    backBinding.cardLayout.setRotationY(value);
                } else {
                    backBinding.cardLayout.setVisibility(View.INVISIBLE);
                    frontBinding.cardLayout.setVisibility(View.VISIBLE);
                    frontBinding.cardLayout.setRotationY(value - 180);
                }
            }
        });
        animator.start();
    }
    public void test(){
        frontBinding.cardLayout.setVisibility(View.VISIBLE);
        frontBinding.cardLayout.setRotationY(0);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
        animator.setDuration(1000);
        animator.setInterpolator(new BaseInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        animator.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                float startFloat = ((Number) startValue).floatValue();
                return startFloat + fraction * (((Number) endValue).floatValue() - startFloat);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                frontBinding.cardLayout.setRotationY(value);
            }
        });
        animator.start();
    }
    public void startRote() {
        frontBinding.cardLayout.setVisibility(View.VISIBLE);
        frontBinding.cardLayout.setRotationY(0);
        backBinding.cardLayout.setRotationY(-90);
        backBinding.cardLayout.setVisibility(View.INVISIBLE);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 180);
        animator.setDuration(500);
        animator.setInterpolator(new BaseInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                if (value <= 90) {
                    frontBinding.cardLayout.setRotationY(value);
                } else {
                    frontBinding.cardLayout.setVisibility(View.INVISIBLE);
                    backBinding.cardLayout.setVisibility(View.VISIBLE);
                    backBinding.cardLayout.setRotationY(value - 180);
                }
            }
        });
        animator.start();
    }
}
