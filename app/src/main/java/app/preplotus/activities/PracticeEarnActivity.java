package app.preplotus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.preplotus.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PracticeEarnActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_earn);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = PracticeEarnActivity.this;

    }


    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.tvInviteNow)
    public void onInvite() {
        Intent in = new Intent(mContext,ReferEarnActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.tvCoinBalance)
    public void onCoinBalance() {
        Intent in = new Intent(mContext,MyCoinsActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.tvAttemptNow)
    public void onTests() {
        Intent intent = new Intent("change");
        intent.putExtra("stat", "1");
        sendBroadcast(intent);
        finish();
    }

}
