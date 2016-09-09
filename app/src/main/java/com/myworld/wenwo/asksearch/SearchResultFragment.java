package com.myworld.wenwo.asksearch;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myworld.wenwo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/9/2.
 */

public class SearchResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchResultFragment.RecyclerAdapter adapter;
    private LinearLayout emptyLayout;

    public static SearchResultFragment newInstance() {

        Bundle args = new Bundle();


        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frgment_search_result, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) root.findViewById(R.id.empty_layout);
        List<String> data = new ArrayList<>();
        adapter = new RecyclerAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                ((AskSearchActivity) getActivity()).dosearch(adapter.getItem(i));
            }
        });
        return root;
    }

    public void setDataList(List<String> results) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        adapter.setNewData(results);
    }

    public void setEmptyData() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    class RecyclerAdapter extends BaseQuickAdapter<String> {


        public RecyclerAdapter(Context context, List<String> data) {
            super(context, R.layout.search_keywords_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.text, s);
        }


    }
}
