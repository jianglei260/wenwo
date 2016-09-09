package com.myworld.wenwo.topics;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityTopicBinding;
import com.myworld.wenwo.view.widget.TitleBar;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTopicBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_topic);
        binding.setViewModel(new TopicViewModel(this));
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
