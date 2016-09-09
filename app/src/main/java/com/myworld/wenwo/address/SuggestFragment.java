package com.myworld.wenwo.address;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.FragmentSearchSuggestBinding;

/**
 * Created by jianglei on 16/9/1.
 */

public class SuggestFragment extends Fragment {
    public static SuggestFragment newInstance() {

        Bundle args = new Bundle();

        SuggestFragment fragment = new SuggestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSearchSuggestBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_suggest, container, false);
        binding.setViewModel(new AddressSearchViewModel(getActivity()));
        return binding.getRoot();
    }
}
