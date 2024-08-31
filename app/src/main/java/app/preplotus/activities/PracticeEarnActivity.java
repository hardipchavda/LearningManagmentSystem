package app.preplotus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.preplotus.R;

import app.preplotus.databinding.ActivityNotesBinding;
import app.preplotus.databinding.ActivityPracticeEarnBinding;


public class PracticeEarnActivity extends AppCompatActivity {

    private Context mContext;

    private ActivityPracticeEarnBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeEarnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = PracticeEarnActivity.this;

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tvInviteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvite();
            }
        });

        binding.tvCoinBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCoinBalance();
            }
        });

        binding.tvAttemptNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTests();
            }
        });

    }




    public void onInvite() {
        Intent in = new Intent(mContext,ReferEarnActivity.class);
        startActivity(in);
    }


    public void onCoinBalance() {
        Intent in = new Intent(mContext,MyCoinsActivity.class);
        startActivity(in);
    }


    public void onTests() {
        Intent intent = new Intent("change");
        intent.putExtra("stat", "1");
        sendBroadcast(intent);
        finish();
    }

}
