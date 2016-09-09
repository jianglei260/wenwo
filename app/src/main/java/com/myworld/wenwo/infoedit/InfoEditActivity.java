package com.myworld.wenwo.infoedit;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityInfoEditBinding;

public class InfoEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInfoEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info_edit);
        binding.setInfoEditViewModel(new InfoEditViewModel(this));

    }
}
