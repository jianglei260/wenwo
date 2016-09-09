package com.myworld.wenwo.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bought.BoughtActivity;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.data.entity.User;
import com.myworld.wenwo.data.entity.UserInfo;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.login.LoginActivity;
import com.myworld.wenwo.main.MainActivity;
import com.myworld.wenwo.shared.SharedActivity;
import com.myworld.wenwo.web.WebActivity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/22.
 */

public class UserViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableInt likeNum = new ObservableInt();
    public ObservableInt sendNum = new ObservableInt();
    public ObservableFloat money = new ObservableFloat();
    public ObservableInt buyNum = new ObservableInt();
    public ObservableField<String> user = new ObservableField<>(Config.USERNAME);
    private UserInfo userInfo;
    public ReplyCommand imageClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand likeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            ((UserActivity) context).finish();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getInstance().onEevent(MainActivity.class, "selectTab", 1);
                }
            }, 100);

        }
    });
    public ReplyCommand sharedClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (userInfo == null)
                return;
            Intent intent = new Intent(context, SharedActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand havedClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (userInfo == null)
                return;
            Intent intent = new Intent(context, BoughtActivity.class);
            context.startActivity(intent);
        }
    });

    public ReplyCommand logoutClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            new AlertDialog.Builder(context).setMessage(R.string.confirm_logout).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Config.USERNAME = "";
                    UserRepository.getInstance().saveUserToLocal(context, "");
                    ((UserActivity) context).recreate();
                }
            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    });
    public ReplyCommand callbackClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            WebActivity.startWebActivity(context, "http://form.mikecrm.com/Qg7fK7", "意见反馈");
        }
    });
    public ReplyCommand shareToClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            showShare();
        }
    });

    private void showShare() {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();

        oks.disableSSOWhenAuthorize();
        oks.setTitle(context.getString(R.string.slogan));
        oks.setText(context.getString(R.string.slogan));
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl("http://www.wenwobei.com/img/logo.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.wenwobei.com");
        oks.show(context);
    }

    public UserViewModel(Context context, UserInfo userInfo) {
        this.context = context;
        this.userInfo = userInfo;
        if (userInfo != null) {
            imageUrl.set(userInfo.getUserHead());
            userName.set(userInfo.getUserShowName());
            likeNum.set(userInfo.getFoodLikeListCount());
            sendNum.set(userInfo.getAskListCount());
            buyNum.set(userInfo.getBuyListCount());
            money.set(userInfo.getTotalIncome());
            user.set(Config.USERNAME);
        }
    }
}
