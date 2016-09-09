package com.myworld.wenwo.found;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.wenwo.R;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.common.viewmodel.ListItemViewModel;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.FragmentFoundBinding;
import com.myworld.wenwo.main.MainActivity;

import java.util.List;


/**
 * Created by jianglei on 16/7/16.
 */

public class FoundFragment extends Fragment implements GestureDetector.OnDoubleTapListener, AskViewModel.AskDataInterface {
    FragmentFoundBinding binding;
    AskViewModel askViewModel;
    RecyclerView recyclerView;

    public static FoundFragment newInstance() {
        FoundFragment fragment = new FoundFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_found, container, false);
        askViewModel = new AskViewModel(getActivity(), this);
        binding.setViewModel(askViewModel);
        ((MainActivity) getActivity()).addDoubleClickListener(this);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.recycler_view);
        askViewModel.setRecyclerView(recyclerView);
        EventBus.getInstance().regist(this);
        final ListItemViewModel bannerItemViewModel = new BannerItemViewModel(getActivity());
        final ListItemViewModel navigationViewModel = new NavigationItemViewModel(getActivity());
        final ListItemViewModel locationViewModel = new LocationViewModel(getActivity(), askViewModel);

        navigationViewModel.viewType.set(ListItemViewModel.VIEW_TYPE_NAVIGATION);
        bannerItemViewModel.viewType.set(ListItemViewModel.VIEW_TYPE_BANNER);
        locationViewModel.viewType.set(ListItemViewModel.VIEW_TYPE_LOCATION);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                askViewModel.addItemViewModel(locationViewModel,0);
                askViewModel.addItemViewModel(navigationViewModel, 0);
                askViewModel.addItemViewModel(bannerItemViewModel, 0);
            }
        });
        return binding.getRoot();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        recyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public List<AskMe> dataSet(int page) {
        if (page == 0) {

        }
        return AskMeRepository.getInstance().getAllAsk(1, page, 20);
    }
    @OnEvent("reload")
    public void reload(){
        askViewModel.refresh();
    }

    @OnEvent("notifyItemChanged")
    public void notifyItemChanged(int position) {
        if (position >= 0) {
            askViewModel.itemChanged(position);
            if (recyclerView != null)
                recyclerView.getAdapter().notifyItemChanged(position);
        }
    }

    @Override
    public List<AskMe> refresh() {
        return AskMeRepository.getInstance().getAllFromRemote(1, 0, 20);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void remove(AskMe askMe) {

    }
}
