package com.myworld.wenwo.kinds;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityKindsBinding;
import com.myworld.wenwo.view.widget.TitleBar;

public class KindsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityKindsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_kinds);
        binding.setViewModel(new KindsViewModel(this));

        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
