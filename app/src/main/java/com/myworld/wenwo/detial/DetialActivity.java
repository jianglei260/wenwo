package com.myworld.wenwo.detial;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.common.BaseActivity;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.ActivityDetialBinding;
import com.myworld.wenwo.databinding.DetailTagAddItemBinding;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.found.LikeStateChangedListener;
import com.myworld.wenwo.utils.MD5;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.view.widget.TitleBar;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import rx.Observable;
import rx.Subscriber;

public class DetialActivity extends BaseActivity implements LikeStateChangedListener {
    public static final String OBJTCT_ID = "objectId";
    public static final String POSITION = "position";
    private boolean isShown;
    private PopupWindow popupWindow;
    private DetialViewModel viewModel;
    private IWXAPI api;
    private Object askViewModel;
    private String objectId;
    private ActivityDetialBinding dataBinding;
    private int position;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectId = getIntent().getStringExtra(OBJTCT_ID);
        position = getIntent().getIntExtra(POSITION, -1);
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        Uri uri = intent.getData();
        if (uri != null) {
            if (uri.getPath().equals("/detail")) {
                objectId = uri.getQueryParameter("askid");
            }
        }
        AskMe askMe = AskMeRepository.getInstance().getAsk(objectId);
        if (askMe == null) {
            getAskMeAndBind(objectId);
        }
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detial);
        viewModel = new DetialViewModel(this, askMe, position);
        viewModel.setLikeStateChangedListener(this);
        dataBinding.setDetialViewModel(viewModel);

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShown)
                    showEditWindow(v);
                else {
                    popupWindow.dismiss();
                    isShown = false;
                }
            }
        });
        LinearLayout content = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.edit_layout, null);
        popupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);

        content.findViewById(R.id.edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.editAsk();
            }
        });
        content.findViewById(R.id.delete_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteAsk();
            }
        });
        EventBus.getInstance().regist(this);
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID);
    }

    public void getAskMeAndBind(final String objectId) {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<AskMe>() {
            @Override
            public void call(Subscriber<? super AskMe> subscriber) {
                subscriber.onNext(AskMeRepository.getInstance().getAskDetail(Config.USERNAME, objectId));
            }
        }, new Subscriber<AskMe>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AskMe askMe) {
                dataBinding.setDetialViewModel(new DetialViewModel(DetialActivity.this, askMe, position));
            }
        });
    }

    @Override
    public void onObjectSet(Object askViewModel) {
        this.askViewModel = askViewModel;
    }

    public void pay(String prepayId, String nonceStr, String timeStamp, String paySign) {
        Toast.makeText(this, R.string.open_weixin_pay, Toast.LENGTH_SHORT).show();
        PayReq request = new PayReq();
        request.appId = Config.APP_ID;
        Log.d("App_id__________", Config.APP_ID);
        request.partnerId = "1373092602";
        request.prepayId = prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        String str = "appid=" + request.appId + "&noncestr=" + request.nonceStr + "&package=" + request.packageValue + "&partnerid=" + request.partnerId
                + "&prepayid=" + request.prepayId + "&timestamp=" + request.timeStamp;
        String keyStr = str + "&key=myworldwenwo20151016myworldwenwo";
        request.sign = MD5.getMessageDigest(keyStr.getBytes());

        api.sendReq(request);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @OnEvent("deleteComplete")
    public void deleteComplete() {
        if (askViewModel instanceof AskViewModel)
            ((AskViewModel) askViewModel).removeItem(objectId);
        finish();
    }

    @OnEvent("sendComplete")
    public void sendComplete() {
        viewModel.updateDetail();
    }

    @Override
    public void finish() {
        if (popupWindow.isShowing())
            popupWindow.dismiss();
        super.finish();
    }

    public void showEditWindow(View view) {
        popupWindow.showAsDropDown(view);
        isShown = true;

    }

    public void itemChanged(int position) {
        if (askViewModel instanceof AskViewModel)
            ((AskViewModel) askViewModel).itemChanged(position);
    }

    @Override
    public void liked(int position) {
        if (askViewModel instanceof LikeStateChangedListener)
            ((LikeStateChangedListener) askViewModel).liked(position);
    }

    @Override
    public void disLiked(int position) {
        ((LikeStateChangedListener) askViewModel).disLiked(position);
        if (askViewModel instanceof AskViewModel)
            if (((AskViewModel) askViewModel).getStateChangeListener() != null)
                ((AskViewModel) askViewModel).getStateChangeListener().removeItem(objectId);
    }
}
