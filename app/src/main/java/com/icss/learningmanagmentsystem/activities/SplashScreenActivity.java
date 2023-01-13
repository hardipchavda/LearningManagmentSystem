package com.icss.learningmanagmentsystem.activities;

import static com.icss.learningmanagmentsystem.utilities.Constants.CATEGORY_ID;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.icss.learningmanagmentsystem.R;
import com.icss.learningmanagmentsystem.utilities.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Utils.getPrefData(USER_ID, SplashScreenActivity.this).trim().length() == 0) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginOptionsActivity.class);
                    startActivity(intent);
                } else if (Utils.getPrefData(CATEGORY_ID, SplashScreenActivity.this).trim().length() == 0) {
                    Intent intent = new Intent(SplashScreenActivity.this, SuperGroupsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();

            }
        }, 1500);

    }
}