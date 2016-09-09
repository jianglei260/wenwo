package com.myworld.wenwo.asksearch;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.FragmentAskResultBinding;
import com.myworld.wenwo.found.AskViewModel;

import java.util.List;

/**
 * Created by jianglei on 16/9/2.
 */

public class AskResultFragment extends Fragment implements AskViewModel.AskDataInterface {
    private AskViewModel askViewModel;
    private String keyword;

    public static AskResultFragment newInstance() {

        Bundle args = new Bundle();

        AskResultFragment fragment = new AskResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAskResultBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ask_result, container, false);
        askViewModel = new AskViewModel(getActivity(), this);
        binding.setViewModel(askViewModel);
        return binding.getRoot();
    }

    public void doSearch(String keyword) {
        this.keyword = keyword;
        askViewModel.refresh();
    }

    @Override
    public List<AskMe> dataSet(int page) {
        return AskMeRepository.getInstance().searchAsk(Config.USERNAME, page, Config.PAGE_SIZE, keyword);
    }

    @Override
    public List<AskMe> refresh() {
        List<AskMe> results = AskMeRepository.getInstance().searchAsk(Config.USERNAME, 0, Config.PAGE_SIZE, keyword);
        if (results.size() <= 0)
            ((AskSearchActivity) getActivity()).showEmptyView();
        return results;
    }

    @Override
    public void remove(AskMe askMe) {

    }
}
