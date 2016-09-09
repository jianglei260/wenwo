package com.myworld.wenwo.found;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.common.viewmodel.ListItemViewModel;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.detial.DetialActivity;
import com.myworld.wenwo.like.LikeFragment;
import com.myworld.wenwo.listask.ListAskActivity;
import com.myworld.wenwo.login.CheckForLogin;
import com.myworld.wenwo.utils.ActivityTool;
import com.myworld.wenwo.utils.LocationUtil;
import com.myworld.wenwo.utils.ObservableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/7/16.
 */

public class AskItemViewModel extends ListItemViewModel implements ViewModel, LikeStateChangedListener {
    public AskMe askMe;
    protected Context context;
    public ObservableField<String> likeNum = new ObservableField<String>();
    public ObservableField<String> imageUrl = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> desc = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<String>();
    public ObservableList<String> title = new ObservableArrayList<>();
    public ObservableField<String> bigImage = new ObservableField<>();
    public ObservableBoolean liked = new ObservableBoolean(false);
    public ObservableInt score = new ObservableInt();
    public ObservableField<String> district = new ObservableField<String>();
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> lookNum = new ObservableField<>();

    public ReplyCommand itemClickCommand = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Intent intent = new Intent(context, DetialActivity.class);
            intent.putExtra(DetialActivity.OBJTCT_ID, askMe.getObjectId());
            intent.putExtra(DetialActivity.POSITION, position);
            ActivityTool.startActivity(context, DetialActivity.class, intent, likeStateChangedListener);
        }
    });
    public ReplyCommand likeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(Config.USERNAME)) {
                CheckForLogin.needLogin(context);
                return;
            }
            if (liked.get())
                cancelLike();
            else
                like();
        }
    });

    public ReplyCommand tag0Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            openTag(0);
        }
    });
    public ReplyCommand tag1Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            openTag(1);
        }
    });
    public ReplyCommand tag2Click = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            openTag(2);
        }
    });
    private AskStateChangeListener stateChangeListener;

    public void setStateChangeListener(AskStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    private void openTag(int position) {
        if (title.size() <= 0)
            return;
        final ObservableField<String> tagText = new ObservableField<>(title.get(position));
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

    protected void like() {
        liked.set(true);
        likeNum.set(askMe.getLikeNum() + 1 + "");
        askMe.setLiked(1);
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean success = AskMeRepository.getInstance().like(Config.USERNAME, askMe.getObjectId());
                subscriber.onNext(success);
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
                    liked.set(true);
                    if (likeStateChangedListener != null) {
                        likeStateChangedListener.liked(position);
                    }
//                    EventBus.getInstance().onEevent(FoundFragment.class, "notifyItemChanged", position);
                } else {
                    liked.set(false);
                    likeNum.set(askMe.getLikeNum() + "");
                    askMe.setLiked(0);
                }

            }
        });
    }


    protected void cancelLike() {
        liked.set(false);
        likeNum.set(askMe.getLikeNum() - 1 + "");
        askMe.setLiked(0);
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean success = AskMeRepository.getInstance().dislike(Config.USERNAME, askMe.getObjectId());
                subscriber.onNext(success);
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
                    liked.set(false);
                    if (likeStateChangedListener != null) {
                        likeStateChangedListener.disLiked(position);
                    }
                    if (stateChangeListener != null)
                        stateChangeListener.removeItem(askMe.getObjectId());
//                    EventBus.getInstance().onEevent(FoundFragment.class, "notifyItemChanged", position);
                } else {
                    liked.set(true);
                    likeNum.set(askMe.getLikeNum() + "");
                    askMe.setLiked(1);
                }

            }
        });
    }

    protected int position;
    protected LikeStateChangedListener likeStateChangedListener;

    public LikeStateChangedListener getLikeStateChangedListener() {
        return likeStateChangedListener;
    }

    public void setLikeStateChangedListener(LikeStateChangedListener likeStateChangedListener) {
        this.likeStateChangedListener = likeStateChangedListener;
    }

    public AskItemViewModel(Context context, AskMe askMe, int position) {
        this.askMe = askMe;
        this.context = context;
        this.position = position;
        if (askMe == null) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(askMe.getAskImage())) {
            try {
                JSONArray array = new JSONArray(askMe.getAskImage());
                bigImage.set(array.getJSONObject(0).getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        distance.set(LocationUtil.getFormatedDistance(Config.geo_latitude, Config.geo_longtitude, askMe.getGeoX(), askMe.getGeoY()));
        score.set(askMe.getScore());
        likeNum.set(String.valueOf(askMe.getLikeNum()));
        lookNum.set("" + askMe.getLookNum());
        imageUrl.set(askMe.getCreateByUrl());
        String formatDate = askMe.getCreatedAt().split("T")[0];
        date.set(formatDate);
        setDistrictText();
        liked.set(askMe.getLiked() > 0);
        desc.set(askMe.getAskReason());
        try {
            JSONArray tags = new JSONArray(askMe.getAskTagStr());
            for (int i = 0; i < tags.length(); i++) {
                String tag = tags.getJSONObject(i).getString("tag_name");
                title.add(tag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (askMe.getAskPrice() == 0)
            price.set("免费瞅瞅");
        else
            price.set(String.valueOf(askMe.getAskPrice()) + "元瞅瞅");

    }

    private void setDistrictText() {
        try {
            JSONObject addressObjec = new JSONObject(askMe.getAskPosition());
            String districtText = addressObjec.getString("district");
            district.set(districtText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dataChanged() {
        askMe = AskMeRepository.getInstance().getAskFromCache(askMe.getObjectId());
        liked.set(askMe.getLiked() > 0);
        likeNum.set(String.valueOf(askMe.getLikeNum()));
    }

    @Override
    public void liked(int position) {
        askMe = AskMeRepository.getInstance().getAskFromCache(askMe.getObjectId());
        liked.set(askMe.getLiked() > 0);
        likeNum.set(String.valueOf(askMe.getLikeNum()));
        if (likeStateChangedListener != null)
            likeStateChangedListener.liked(position);
    }

    @Override
    public void disLiked(int position) {
        askMe = AskMeRepository.getInstance().getAskFromCache(askMe.getObjectId());
        liked.set(askMe.getLiked() > 0);
        likeNum.set(String.valueOf(askMe.getLikeNum()));
        if (likeStateChangedListener != null)
            likeStateChangedListener.disLiked(position);
    }
}
