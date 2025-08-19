package app.preplotus.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensures UI is placed below status bar across all activities
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
    }
}