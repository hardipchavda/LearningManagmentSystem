package com.icss.learningmanagmentsystem.activities;

import static com.icss.learningmanagmentsystem.utilities.Constants.NOTI_FLAG;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.icss.learningmanagmentsystem.R;
import com.icss.learningmanagmentsystem.model.GeneralResponse;
import com.icss.learningmanagmentsystem.network.APIClient;
import com.icss.learningmanagmentsystem.network.APIInterface;
import com.icss.learningmanagmentsystem.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
