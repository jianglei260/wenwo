package com.myworld.wenwo.view.widget;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myworld.wenwo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/7/19.
 */

public class ViewPagerIndicator extends FrameLayout {
    private ViewPager viewPager;
    private LinearLayout container;
    private List<IndicatorItem> items;
    private int selectedIndex = 0;
    private View line;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initUI() {
        container = new LinearLayout(getContext());
        items = new ArrayList<>();
        line = new View(getContext());
        line.setBackgroundColor(Color.parseColor("#dddddd"));
        int height = getResources().getDimensionPixelSize(R.dimen.line_width);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.indicator_line_top_margin);
        addView(line, params);
        addView(container);
    }

    public ViewPagerIndicator addItem(IndicatorItem item) {
        items.add(item);
        return this;
    }

    class Builder {
        public Builder() {
        }

        public Builder addItem(IndicatorItem item) {
            items.add(item);
            return this;
        }

        public ViewPagerIndicator build() {
            return ViewPagerIndicator.this;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initialise() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < items.size(); i++) {
            IndicatorItem item = items.get(i);
            LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.indicator_item, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            ImageView imageView = (ImageView) itemLayout.findViewById(R.id.icon);
            ImageView dotImage = (ImageView) itemLayout.findViewById(R.id.dot);
            TextView textView = (TextView) itemLayout.findViewById(R.id.title);
            imageView.setImageResource(item.getImageRes());
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(item.getColor());
            dotImage.setBackground(drawable);
            textView.setText(item.getTitle());
            imageView.setVisibility(INVISIBLE);
            textView.setVisibility(INVISIBLE);
            container.addView(itemLayout, params);
            int finalI = i;
            itemLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectIndex(finalI);
                }
            });
        }
        setSelectIndex(0);
    }

    public void setSelectIndex(final int index) {
        if (index > items.size() || index < 0)
            throw new IndexOutOfBoundsException("index is out of bounds");
        LinearLayout itemLayout = (LinearLayout) container.getChildAt(selectedIndex);
        hideItem(itemLayout);
        LinearLayout selectItem = (LinearLayout) container.getChildAt(index);
        showItem(selectItem);
        selectedIndex = index;
        if (viewPager != null && viewPager.getAdapter().getCount() == items.size()) {
            viewPager.setCurrentItem(index);
        }
    }

    private void hideItem(LinearLayout itemLayout) {
        ImageView imageView = (ImageView) itemLayout.findViewById(R.id.icon);
        ImageView dotImage = (ImageView) itemLayout.findViewById(R.id.dot);
        TextView textView = (TextView) itemLayout.findViewById(R.id.title);
        dotImage.setVisibility(VISIBLE);
        imageView.setVisibility(INVISIBLE);
        textView.setVisibility(INVISIBLE);
    }

    private void showItem(LinearLayout itemLayout) {
        ImageView imageView = (ImageView) itemLayout.findViewById(R.id.icon);
        ImageView dotImage = (ImageView) itemLayout.findViewById(R.id.dot);
        TextView textView = (TextView) itemLayout.findViewById(R.id.title);
        dotImage.setVisibility(INVISIBLE);
        imageView.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
    }

    private boolean layouted = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = container.getChildCount();
        if (count <= 0)
            return;
        if (!layouted) {
            View first = container.getChildAt(0);
            View last = container.getChildAt(count - 1);
            float firstX = first.getX();
            float lastX = last.getX();
            line.setMinimumWidth((int) (lastX - firstX));
            int height = getResources().getDimensionPixelSize(R.dimen.line_width);
            LayoutParams params = new LayoutParams((int) (lastX - firstX), height);
            params.topMargin = getResources().getDimensionPixelSize(R.dimen.indicator_line_top_margin);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            line.setLayoutParams(params);
            layouted = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}
