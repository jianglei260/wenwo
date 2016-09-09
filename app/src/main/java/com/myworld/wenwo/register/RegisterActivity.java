package com.myworld.wenwo.register;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityRegisterBinding;
import com.myworld.wenwo.login.LoginViewModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setRegisterViewModel(new RegisterViewModel(this));
    }
}
