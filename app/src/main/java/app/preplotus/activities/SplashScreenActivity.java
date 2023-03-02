package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_IMAGE;
import static app.preplotus.utilities.Constants.USER_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import app.preplotus.R;
import app.preplotus.utilities.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        Utils.setPrefData(USER_ID, "1", SplashScreenActivity.this);
//        Utils.setPrefData(CATEGORY_ID, "1", SplashScreenActivity.this);

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