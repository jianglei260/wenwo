package com.myworld.wenwo.asksearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.myworld.wenwo.R;
import com.myworld.wenwo.common.BaseActivity;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.utils.ObservableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class AskSearchActivity extends BaseActivity {
    private SearchSuggestFragment suggestFragment;
    private SearchResultFragment resultFragment;
    private AskResultFragment askResultFragment;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_search);
        suggestFragment = SearchSuggestFragment.newInstance();
        resultFragment = SearchResultFragment.newInstance();
        askResultFragment = AskResultFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container, resultFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.container, suggestFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.container, askResultFragment).commit();
        getFragmentManager().beginTransaction().hide(resultFragment).commit();
        getFragmentManager().beginTransaction().hide(askResultFragment).commit();
        getFragmentManager().beginTransaction().show(suggestFragment).commit();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editText = (EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    findKeyWords(s.toString());
                else {
                    getFragmentManager().beginTransaction().show(suggestFragment).commit();
                    getFragmentManager().beginTransaction().hide(resultFragment).commit();
                    getFragmentManager().beginTransaction().hide(askResultFragment).commit();
                }
            }
        });
        findViewById(R.id.search_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0){
                   dosearch(editText.getText().toString());
                }

            }
        });
    }
    public void dosearch(String keyword){
        getFragmentManager().beginTransaction().show(askResultFragment).commit();
        getFragmentManager().beginTransaction().hide(resultFragment).commit();
        getFragmentManager().beginTransaction().hide(suggestFragment).commit();
        askResultFragment.doSearch(keyword);
    }
    public void showEmptyView() {
        getFragmentManager().beginTransaction().hide(suggestFragment).commit();
        getFragmentManager().beginTransaction().hide(askResultFragment).commit();
        getFragmentManager().beginTransaction().show(resultFragment).commit();
        resultFragment.setEmptyData();
    }

    public void findKeyWords(final String keyword) {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                String result = AskMeRepository.getInstance().getKeyWords(keyword);
                List<String> words = new ArrayList<String>();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") != 200) {
                        subscriber.onError(new Exception("request error"));
                        return;
                    }
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        String word = array.getJSONObject(i).getString("tagName");
                        words.add(word);
                    }
                    subscriber.onNext(words);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                if (strings.size() > 0) {
                    getFragmentManager().beginTransaction().hide(suggestFragment).commit();
                    getFragmentManager().beginTransaction().show(resultFragment).commit();
                    resultFragment.setDataList(strings);
                }

            }
        });
    }
}
