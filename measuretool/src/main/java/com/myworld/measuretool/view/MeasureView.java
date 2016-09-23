package com.myworld.measuretool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.myworld.measuretool.R;

/**
 * Created by jianglei on 16/9/13.
 */

public class MeasureView extends View {
    private int defaultHeight;
    private Paint paint;
    private int dp;
    private float scale;
    private boolean rotated;

    public MeasureView(Context context) {
        this(context, null);
    }

    public MeasureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5.0f);
        paint.setTextSize(24);
        defaultHeight = context.getResources().getDimensionPixelSize(R.dimen.default_height);
        dp = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
        scale = context.getResources().getDisplayMetrics().density;
    }

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated, ViewGroup root) {
        if (this.rotated == rotated) {
            return;
        }
        int width = getWidth();
        int height = getHeight();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(height, width);
        setLayoutParams(layoutParams);
        invalidate();
        this.rotated = rotated;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!rotated) {
            int width = (int) (getWidth() / (scale * 10) + 0.5f);
            for (int i = 0; i <= width; i++) {
                int unitHeight = 20;
                if (i % 5 == 0)
                    unitHeight = 30;
                if (i % 10 == 0)
                    unitHeight = 40;
                canvas.drawLine(getLeft() + 10 * i * dp, getTop(), getLeft() + 10 * i * dp, getTop() + unitHeight, paint);
                canvas.drawText(String.valueOf(i), getLeft() + 10 * i * dp, getTop() + 40, paint);
            }
        } else {
//            canvas.rotate(90);
            int height = (int) (getHeight() / (scale * 10) + 0.5f);
            for (int i = 0; i <= height; i++) {
                int unitHeight = 20;
                if (i % 5 == 0)
                    unitHeight = 30;
                if (i % 10 == 0)
                    unitHeight = 40;
                canvas.drawLine(getLeft(), getTop() + 10 * i * dp, getLeft() + unitHeight, getTop() + 10 * i * dp, paint);
                canvas.drawText(String.valueOf(i), getLeft() + 40, getTop() + 10 * i * dp, paint);
            }
        }


    }

    public void rotate(ViewGroup root) {
        if (rotated) {
            setRotated(false, root);
        } else {
            setRotated(true, root);
        }
    }
}
