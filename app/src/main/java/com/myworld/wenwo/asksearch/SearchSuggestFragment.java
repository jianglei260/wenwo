package com.myworld.wenwo.asksearch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.Tag;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.kinds.KindItemViewModel;
import com.myworld.wenwo.listask.ListAskActivity;
import com.myworld.wenwo.utils.ActivityTool;
import com.myworld.wenwo.utils.ObservableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by jianglei on 16/9/2.
 */

public class SearchSuggestFragment extends Fragment {
    private FlexboxLayout flexboxLayout;

    public static SearchSuggestFragment newInstance() {

        Bundle args = new Bundle();

        SearchSuggestFragment fragment = new SearchSuggestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_ask_search_hot, container, false);
        flexboxLayout = (FlexboxLayout) root.findViewById(R.id.flex_box);
        findHotWords();
        return root;
    }

    public void findHotWords() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<Tag>() {
            @Override
            public void call(Subscriber<? super Tag> subscriber) {
                String result = AskMeRepository.getInstance().getAllTag();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") != 200)
                        return;
                    JSONObject types = object.getJSONObject("type");
                    Iterator<String> typeKeys = types.keys();
                    if (typeKeys.hasNext()) {
                        String type = typeKeys.next();
                        Tag tag = new Tag(type);
                        JSONArray tagArray = types.getJSONArray(type);
                        for (int j = 0; j < tagArray.length(); j++) {
                            String tagText = tagArray.getJSONObject(j).getString("tagName");
                            tag.childs.add(tagText);
                        }
                        subscriber.onNext(tag);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Subscriber<Tag>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Tag tag) {
                for (final String text : tag.childs) {
                    TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.search_item_layout, flexboxLayout, false);
                    textView.setText(text);
                    flexboxLayout.addView(textView);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openTag(text);
                        }
                    });
                }
            }
        });

    }
    public void openTag(final String tagText){
        ActivityTool.startActivityForCallBack(getActivity(), ListAskActivity.class, new ActivityTool.CallBack<ListAskActivity>() {
            @Override
            public void onCreated(ListAskActivity activity) {
                activity.setDataAndTitle(new AskViewModel.AskDataInterface() {
                    @Override
                    public List<AskMe> dataSet(int page) {
                        return AskMeRepository.getInstance().getTagAsk(Config.USERNAME, 1, page, Config.PAGE_SIZE, tagText);
                    }

                    @Override
                    public List<AskMe> refresh() {
                        return AskMeRepository.getInstance().getTagAsk(Config.USERNAME, 1, 0, Config.PAGE_SIZE, tagText);
                    }

                    @Override
                    public void remove(AskMe askMe) {

                    }
                }, tagText);
            }
        });
    }
}
