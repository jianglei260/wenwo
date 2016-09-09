package com.myworld.wenwo.delete;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.myworld.wenwo.R;
import com.myworld.wenwo.add.AddActivity;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.ActivityDeleteBinding;
import com.myworld.wenwo.detial.DetialActivity;
import com.myworld.wenwo.view.widget.TitleBar;

public class DeleteActivity extends AppCompatActivity {
    private DeleteViewModel deleteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String objectId = getIntent().getStringExtra("objectId");
        setContentView(R.layout.activity_delete);
        ActivityDeleteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_delete);
        AskMe askMe = AskMeRepository.getInstance().getAsk(objectId);
        deleteViewModel = new DeleteViewModel(askMe, this);
        binding.setViewModel(deleteViewModel);
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        EventBus.getInstance().regist(this);
    }

    public static void startActivity(Context context, String objectId) {
        Intent intent = new Intent(context, DeleteActivity.class);
        intent.putExtra("objectId", objectId);
        context.startActivity(intent);
    }

    @OnEvent("deleteComplete")
    public void deleteComplete() {
        finish();
        EventBus.getInstance().onEevent(DetialActivity.class,"deleteComplete");
    }
}
