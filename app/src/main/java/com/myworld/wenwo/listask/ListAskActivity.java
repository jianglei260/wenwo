package com.myworld.wenwo.listask;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.common.BaseActivity;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.databinding.ActivityBoughtBinding;
import com.myworld.wenwo.databinding.ActivityListAskBinding;
import com.myworld.wenwo.found.AskViewModel;
import com.myworld.wenwo.view.widget.TitleBar;

import java.util.List;

public class ListAskActivity extends BaseActivity {
    private ActivityListAskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setDataAndTitle(AskViewModel.AskDataInterface askDatainterface, String titleText) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_ask);
        binding.setViewModel(new AskViewModel(this, askDatainterface));
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.setTitleText(titleText);
    }
}
