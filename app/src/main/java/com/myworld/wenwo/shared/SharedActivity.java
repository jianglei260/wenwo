package com.myworld.wenwo.shared;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.ActivitySharedBinding;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.view.widget.TitleBar;

import java.util.List;

public class SharedActivity extends AppCompatActivity implements AskViewModel.AskDataInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySharedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shared);
        AskViewModel askViewModel=new AskViewModel(this, this);
        binding.setViewModel(askViewModel);
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public List<AskMe> dataSet(int page) {
        return AskMeRepository.getInstance().getMyAskList(Config.USERNAME,page);
    }

    @Override
    public List<AskMe> refresh() {
        return AskMeRepository.getInstance().getMyAskList(Config.USERNAME,0);
    }

    @Override
    public void remove(AskMe askMe) {

    }
}
