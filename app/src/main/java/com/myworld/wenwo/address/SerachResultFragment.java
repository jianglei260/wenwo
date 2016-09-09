package com.myworld.wenwo.address;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.found.FoundFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/9/1.
 */

public class SerachResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayout emptyLayout;

    public static SerachResultFragment newInstance() {

        Bundle args = new Bundle();

        SerachResultFragment fragment = new SerachResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frgment_search_result, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) root.findViewById(R.id.empty_layout);
        List<SuggestionResult.SuggestionInfo> data = new ArrayList<>();
        adapter = new RecyclerAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                SuggestionResult.SuggestionInfo info= (SuggestionResult.SuggestionInfo) adapter.getData().get(i);
                Config.geo_latitude=info.pt.latitude;
                Config.geo_longtitude=info.pt.longitude;
                Config.geo_address.set(info.key);
                EventBus.getInstance().onEevent(FoundFragment.class,"reload");
                getActivity().finish();
            }
        });
        return root;
    }

    public void setDataList(List<SuggestionResult.SuggestionInfo> results) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        adapter.setNewData(results);
    }

    public void setEmptyData() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    class RecyclerAdapter extends BaseQuickAdapter<SuggestionResult.SuggestionInfo> {


        public RecyclerAdapter(Context context, List<SuggestionResult.SuggestionInfo> data) {
            super(context, R.layout.list_search_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, SuggestionResult.SuggestionInfo s) {
            baseViewHolder.setText(R.id.text, s.key).setText(R.id.district, s.city + s.district);
        }


    }
}
