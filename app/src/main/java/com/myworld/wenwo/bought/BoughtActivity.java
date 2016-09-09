package com.myworld.wenwo.bought;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.ActivityBoughtBinding;
import com.myworld.wenwo.databinding.ActivitySharedBinding;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.view.widget.TitleBar;

import java.util.List;

public class BoughtActivity extends AppCompatActivity implements AskViewModel.AskDataInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBoughtBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bought);
        binding.setViewModel(new AskViewModel(this, this));
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
        return AskMeRepository.getInstance().getHavedList(Config.USERNAME, page);
    }

    @Override
    public List<AskMe> refresh() {
        return AskMeRepository.getInstance().getHavedList(Config.USERNAME, 0);
    }

    @Override
    public void remove(AskMe askMe) {

    }
}
