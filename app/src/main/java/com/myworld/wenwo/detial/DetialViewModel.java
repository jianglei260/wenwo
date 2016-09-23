package com.myworld.wenwo.detial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.add.AddActivity;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.AuthInfo;
import com.myworld.wenwo.data.entity.DetialTag;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.data.repository.UserRepository;
import com.myworld.wenwo.data.source.local.AuthLocalDataSource;
import com.myworld.wenwo.delete.DeleteActivity;
import com.myworld.wenwo.found.AskItemViewModel;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.found.FoundFragment;
import com.myworld.wenwo.login.CheckForLogin;
import com.myworld.wenwo.login.LoginActivity;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.view.widget.DebaseDialog;
import com.myworld.wenwo.view.widget.WenwoDialog;
import com.myworld.wenwo.vipcard.VipCardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by jianglei on 16/7/18.
 */

public class DetialViewModel extends AskItemViewModel {
    public ObservableField<String> shop = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> detial = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> vip = new ObservableField<>();
    public ObservableBoolean finish = new ObservableBoolean(false);
    public ObservableBoolean haved = new ObservableBoolean(false);
    public ObservableBoolean isOwn = new ObservableBoolean(false);
    public ObservableField<String> underReason = new ObservableField<>();

    public ObservableList<DetialTagViewModel> tags = new ObservableArrayList<>();
    public final ItemViewSelector<DetialTagViewModel> itemView = new ItemViewSelector<DetialTagViewModel>() {
        @Override
        public void select(ItemView itemView, int position, DetialTagViewModel item) {
            itemView.setLayoutRes(R.layout.detial_tag_item);
            itemView.setBindingVariable(BR.itemViewModel);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ReplyCommand vipClick =new ReplyCommand(new Action0() {
        @Override
        public void call() {
            VipCardDialog dialog=new VipCardDialog(context,askMe);
            dialog.show();
        }
    });
    public ReplyCommand buyClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(Config.USERNAME)) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CheckForLogin.needLogin(context);
                    }
                });
                return;
            }
            if (isOwn.get() || haved.get())
                return;
            buyAsk();
        }
    });

    public ReplyCommand debaseClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            showDebaseDialog();
        }
    });

    protected boolean reLogin(AuthInfo authInfo) {
        if (authInfo == null)
            return true;
        return System.currentTimeMillis() - authInfo.getExpiresTime() > (authInfo.getExpiresIn() - 20) * 1000;
    }

    public void doLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void buyAsk() {
        final WenwoDialog dialog = new WenwoDialog(context);
        dialog.show(context.getString(R.string.pay_ing));
        ObservableUtil.runOnUI(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                AuthInfo info = AuthLocalDataSource.getAuthInfo(context);
                if (reLogin(info)) {
                    ((DetialActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, context.getString(R.string.auth_invalid), Toast.LENGTH_SHORT).show();
                            doLogin();
                            dialog.dismiss();
                        }
                    });
                    return;
                } else {
                    subscriber.onNext(AskMeRepository.getInstance().getOrder(info.getOpenid(), info.getToken(), info.getExpiresIn(), askMe.getObjectId(), Config.USERNAME));
                }
            }
        }, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("pay___order____", s);
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(context, context.getString(R.string.pre_pay_failed), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getInt("code") == 100) {
                        updateDetail();
                    }
                    if (object.getInt("code") == 200) {
                        JSONObject payArgs = object.getJSONObject("payargs");
                        String timeStamp = payArgs.getString("timeStamp");
                        String nonceStr = payArgs.getString("nonceStr");
                        String prepayId = payArgs.getString("package").split("=")[1];
                        String paySign = payArgs.getString("paySign");
                        ((DetialActivity) context).pay(prepayId, nonceStr, timeStamp, paySign);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnEvent("wxPayComplete")
    public void wxPayComplete() {
        Toast.makeText(context, context.getString(R.string.weixin_pay_complete), Toast.LENGTH_SHORT).show();
        updateDetail();
    }

    public DetialViewModel(Context context, AskMe askMe, int position) {
        super(context, askMe, position);
        updateDetail();
        addLook();
        EventBus.getInstance().regist(this);
        if (askMe == null) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
            return;
        }
        bindData(askMe);
    }

    public void showDebaseDialog() {
        final DebaseDialog debaseDialog = new DebaseDialog(context);
        debaseDialog.show();
        debaseDialog.setSendListener(new DebaseDialog.SendListener() {
            @Override
            public void sendText(String text) {
                debase(text);
                debaseDialog.dismiss();
            }
        });
    }

    void bindData(AskMe askMe) {
        if (!TextUtils.isEmpty(askMe.getAskImage())) {
            try {
                JSONArray array = new JSONArray(askMe.getAskImage());
                imageUrl.set(array.getJSONObject(0).getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(askMe.getVipCardContent())){
            vip.set("res:///"+R.drawable.vip);
        }
        isOwn.set(askMe.getCreateBy().equals(Config.USERNAME));
        userName.set(askMe.getCreateByName());
        underReason.set(askMe.getAskDefault());
        score.set(askMe.getScore());
        lookNum.set("" + askMe.getLookNum());
        haved.set(askMe.isHaved());
        liked.set(askMe.getLiked() > 0);
        likeNum.set(String.valueOf(askMe.getLikeNum()));
        shop.set(askMe.getShopName());

        if (askMe.getAskPrice() <= 0)
            price.set(context.getString(R.string.no_charge));
        else
            price.set(askMe.getAskPrice() + "元查看");
        if (score.get() < 20) {
            price.set(context.getString(R.string.free));
        }
        if (askMe.getCreateBy().equals(Config.USERNAME))
            price.set(context.getString(R.string.mine));
        String detialJson = askMe.getAskContentShow();
        try {
            JSONObject detialObject = new JSONObject(detialJson);
            detial.set(detialObject.getString("detail"));
            JSONArray tagArray = new JSONArray(detialObject.getString("detailLi"));
            tags.clear();
            for (int i = 0; i < tagArray.length(); i++) {
                JSONObject tagObject = tagArray.getJSONObject(i);
                DetialTag detialTag = new DetialTag();
                detialTag.setName(tagObject.getString("name"));
                detialTag.setVal(tagObject.getString("val"));
                DetialTagViewModel detialTagViewModel = new DetialTagViewModel(context, detialTag);
                tags.add(detialTagViewModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAddressDetail();
    }

    private void debase(final String content) {
        if (!TextUtils.isEmpty(Config.USERNAME)) {
            ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    subscriber.onNext(AskMeRepository.getInstance().debase(askMe.getObjectId(), Config.USERNAME, content));
                }
            }, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean success) {
                    if (success) {
                        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.debase_dialog).setView(R.layout.dialog_debase_success).show();
                        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });
        }
    }

    private void addLook() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(AskMeRepository.getInstance().addLook(askMe.getObjectId()));
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }

    private void setAddressDetail() {
        if (score.get() < 20 || askMe.getAskPrice() <= 0 || askMe.isHaved() || isOwn.get())
            try {
                JSONObject addressObjec = new JSONObject(askMe.getAskPosition());
                String province = addressObjec.getString("province");
                String city = addressObjec.getString("city");
                String district = addressObjec.getString("district");
                String township = addressObjec.getString("township");
                String detail = addressObjec.getString("detail");
                String aDetail = "";
                if (addressObjec.has("adetail"))
                    aDetail = addressObjec.getString("adetail");
                address.set(province + city + district + township + detail + aDetail);
            } catch (JSONException e) {
                e.printStackTrace();
                address.set(askMe.getAskPosition());
            }
        else
            address.set(context.getString(R.string.buy_first));
    }

    public void editAsk() {
        AddActivity.startWithEdit(context, askMe);
    }

    public void deleteAsk() {
        DeleteActivity.startActivity(context, askMe.getObjectId());
    }

    void updateDetail() {
        Observable.create(new Observable.OnSubscribe<AskMe>() {
            @Override
            public void call(Subscriber<? super AskMe> subscriber) {
                AskMe newAsk = AskMeRepository.getInstance().getAskDetail(Config.USERNAME, DetialViewModel.this.askMe.getObjectId());
                subscriber.onNext(newAsk);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AskMe>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AskMe askMe) {
                DetialViewModel.this.askMe = askMe;
                bindData(askMe);
                finish.set(true);
                if (likeStateChangedListener != null)
                    ((DetialActivity) likeStateChangedListener).itemChanged(position);
            }
        });
    }
}
