package com.myworld.wenwo.found;

import android.content.Context;
import android.content.Intent;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.card.CardActivity;
import com.myworld.wenwo.common.viewmodel.ListItemViewModel;
import com.myworld.wenwo.kinds.KindsActivity;
import com.myworld.wenwo.topics.TopicActivity;

import java.util.List;

import rx.functions.Action0;

/**
 * Created by jianglei on 16/8/29.
 */

public class NavigationItemViewModel extends ListItemViewModel {
    private Context context;
    public ReplyCommand kindsClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, KindsActivity.class);
            context.startActivity(intent);
        }
    });
    public ReplyCommand cardClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, CardActivity.class);
            context.startActivity(intent);
        }
    });
    public ReplyCommand topicClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, TopicActivity.class);
            context.startActivity(intent);
        }
    });

    public NavigationItemViewModel(Context context) {
        this.context = context;
    }

}
