package com.myworld.wenwo.kinds;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.text.TextUtils;
import android.util.Log;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bought.BoughtActivity;
import com.myworld.wenwo.common.BaseActivity;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.listask.ListAskActivity;
import com.myworld.wenwo.utils.ActivityTool;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/29.
 */

public class KindTagViewModel implements ViewModel {
    public ObservableField<String> tagText = new ObservableField<>();
    public ObservableBoolean isFirstRow = new ObservableBoolean();
    public ObservableBoolean isLastCol = new ObservableBoolean();
    public ObservableBoolean haveBg = new ObservableBoolean();
    public ObservableBoolean isFirstCol = new ObservableBoolean();
    public ObservableBoolean isLastRow = new ObservableBoolean();
    private Context context;
    public ObservableBoolean rightTop = new ObservableBoolean();
    public ObservableBoolean rightBottom = new ObservableBoolean();
    public ObservableBoolean leftBottom = new ObservableBoolean();
    public ReplyCommand clickCommand = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (!TextUtils.isEmpty(tagText.get()))
                ActivityTool.startActivityForCallBack(context, ListAskActivity.class, new ActivityTool.CallBack<ListAskActivity>() {
                    @Override
                    public void onCreated(ListAskActivity activity) {
                        activity.setDataAndTitle(new AskViewModel.AskDataInterface() {
                            @Override
                            public List<AskMe> dataSet(int page) {
                                return AskMeRepository.getInstance().getTagAsk(Config.USERNAME, 1, page, Config.PAGE_SIZE, tagText.get());
                            }

                            @Override
                            public List<AskMe> refresh() {
                                return AskMeRepository.getInstance().getTagAsk(Config.USERNAME, 1, 0, Config.PAGE_SIZE, tagText.get());
                            }

                            @Override
                            public void remove(AskMe askMe) {

                            }
                        }, tagText.get());
                    }
                });
        }
    });

    public KindTagViewModel(Context context, String tag) {
        this.context = context;
        tagText.set(tag);
    }

    public void updateCorner() {

//        ShapeDrawable drawable = (ShapeDrawable) context.getResources().getDrawable(R.drawable.shape_tag_right_top);
//        drawable.getPaint().setColor(context.getResources().getColor(R.color.kind_tag_bg));
//        this.drawable.set(drawable);
        rightTop.set(isFirstRow.get() && isLastCol.get());
        rightBottom.set(isLastRow.get() && isLastCol.get());
        leftBottom.set(isFirstCol.get() && isLastRow.get());
        Log.d(tagText.get(), isFirstRow.get() ? "isfirstrow" : "");
        Log.d(tagText.get(), isFirstCol.get() ? "isfirstcol" : "");
        Log.d(tagText.get(), isLastRow.get() ? "islastrow" : "");
        Log.d(tagText.get(), isLastCol.get() ? "islastcol" : "");
    }
}
