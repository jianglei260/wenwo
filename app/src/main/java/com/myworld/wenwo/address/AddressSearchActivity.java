package com.myworld.wenwo.address;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.myworld.wenwo.R;
import com.myworld.wenwo.common.BaseActivity;

import java.util.List;

public class AddressSearchActivity extends BaseActivity implements OnGetSuggestionResultListener {
    private SuggestFragment suggestFragment;
    private SerachResultFragment resultFragment;
    private EditText editText;
    private SuggestionSearch search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        suggestFragment = SuggestFragment.newInstance();
        resultFragment = SerachResultFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container, resultFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.container, suggestFragment).commit();
        getFragmentManager().beginTransaction().hide(resultFragment).commit();
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
                    search.requestSuggestion(new SuggestionSearchOption().city("成都").keyword(s.toString()));
                else {
                    getFragmentManager().beginTransaction().show(suggestFragment).commit();
                    getFragmentManager().beginTransaction().hide(resultFragment).commit();
                }
            }
        });
        initSearcher();
        findViewById(R.id.search_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void initSearcher() {
        SDKInitializer.initialize(getApplicationContext());
        search = SuggestionSearch.newInstance();
        search.setOnGetSuggestionResultListener(this);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null)
            return;
        if (suggestionResult.getAllSuggestions() == null || suggestionResult.getAllSuggestions().size() <= 0) {
            getFragmentManager().beginTransaction().hide(suggestFragment).commit();
            getFragmentManager().beginTransaction().show(resultFragment).commit();
            resultFragment.setEmptyData();
            return;
        }
        List<SuggestionResult.SuggestionInfo> suggestionInfos = suggestionResult.getAllSuggestions();
        if (suggestionInfos.size() > 0) {
            getFragmentManager().beginTransaction().hide(suggestFragment).commit();
            getFragmentManager().beginTransaction().show(resultFragment).commit();
            resultFragment.setDataList(suggestionInfos);
        }

    }
}
