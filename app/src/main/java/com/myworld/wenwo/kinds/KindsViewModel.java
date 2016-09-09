package com.myworld.wenwo.kinds;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.data.entity.Tag;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.utils.ObservableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jianglei on 16/8/29.
 */

public class KindsViewModel implements ViewModel {
    public Context context;
    public ObservableList<KindItemViewModel> items = new ObservableArrayList<>();
    public ObservableBoolean isRefresh = new ObservableBoolean(true);
    public ItemViewSelector<KindItemViewModel> itemView = new ItemViewSelector<KindItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, KindItemViewModel item) {
            itemView.set(BR.viewModel, R.layout.kinds_item_layout);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public KindsViewModel(Context context) {
        this.context = context;
        findTags();
    }

    public void findTags() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<Tag>>() {
            @Override
            public void call(Subscriber<? super List<Tag>> subscriber) {
                String result = AskMeRepository.getInstance().getAllTag();
                List<Tag> tags = new ArrayList<Tag>();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") != 200)
                        return;
                    JSONObject types = object.getJSONObject("type");
                    Iterator<String> typeKeys = types.keys();
                    while (typeKeys.hasNext()) {
                        String type = typeKeys.next();
                        Tag tag = new Tag(type);
                        JSONArray tagArray = types.getJSONArray(type);
                        for (int j = 0; j < tagArray.length(); j++) {
                            String tagText = tagArray.getJSONObject(j).getString("tagName");
                            tag.childs.add(tagText);
                        }
                        tags.add(tag);
                    }
                    subscriber.onNext(tags);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Subscriber<List<Tag>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Tag> tags) {
                isRefresh.set(false);
                KindItemViewModel last = null;
                for (int i = 0; i < tags.size(); i++) {
                    Tag tag = tags.get(i);
                    KindItemViewModel kindItemViewModel = new KindItemViewModel(context, tag);
                    if (last != null) {
                        kindItemViewModel.lastHaveBg.set(last.lastRowHaveBg.get());
                    }
                    kindItemViewModel.update();
                    last = kindItemViewModel;
                    items.add(kindItemViewModel);

                }


            }
        });
    }
}
