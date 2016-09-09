package com.myworld.wenwo.card;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.databinding.ActivityCardBinding;
import com.myworld.wenwo.view.widget.TitleBar;
import com.wenchao.cardstack.CardStack;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_card);
        binding.setViewModel(new CardViewModel(this));
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public CardStack getCardStack(){
        return (CardStack) findViewById(R.id.view_pager);
    }
}
