package com.myworld.wenwo.like;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.FragmentFoundBinding;
import com.myworld.wenwo.found.AskStateChangeListener;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.found.LikeStateChangedListener;

import java.util.List;

/**
 * Created by jianglei on 16/7/19.
 */

public class LikeFragment extends Fragment implements AskViewModel.AskDataInterface, AskStateChangeListener {
    private RecyclerView recyclerView;
    private AskViewModel askViewModel;

    public static LikeFragment newInstance() {
        LikeFragment fragment = new LikeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_found, container, false);
        askViewModel = new AskViewModel(getActivity(), this);
        askViewModel.setStateChangeListener(this);
        binding.setViewModel(askViewModel);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.recycler_view);
        askViewModel.setRecyclerView(recyclerView);

        return binding.getRoot();
    }

    public void refreshData() {
        askViewModel.refresh();
    }

    @Override
    public List<AskMe> dataSet(int page) {
        return AskMeRepository.getInstance().getLikedListFromRemote(Config.USERNAME, page);
    }

    @Override
    public List<AskMe> refresh() {
        return AskMeRepository.getInstance().getLikedListFromRemote(Config.USERNAME, 0);//// TODO: 16/7/28;
    }

    @OnEvent("notifyItemChanged")
    public void notifyItemChanged(int position) {
        if (position >= 0) {
            askViewModel.itemChanged(position);
            if (recyclerView != null)
                recyclerView.getAdapter().notifyItemChanged(position);
        }
    }

    @OnEvent("notifyItemRemoved")
    public void notifyItemRemoved(int position) {
        if (position >= 0) {
            askViewModel.itemRemoved(position);
            if (recyclerView != null)
                recyclerView.getAdapter().notifyItemRangeRemoved(position, 1);
        }
    }

    @Override
    public void remove(AskMe askMe) {

    }

    @Override
    public void removeItem(String object) {
        askViewModel.removeItem(object);
    }
}
