package app.preplotus.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import app.preplotus.R;
import kotlin.Suppress;

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensures UI is placed below status bar across all activities
        applySystemBarsPolicy();
    }

    @SuppressWarnings("deprecation")
    private void applySystemBarsPolicy() {
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+ (API 30+) — ensure layout does NOT go under system bars
            getWindow().setDecorFitsSystemWindows(true);
        } else {
            // For Android 10 and below
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    private void forceBelowStatusBar() {
        if (Build.VERSION.SDK_INT >= 35) {
            // 1) Framework handles insets (no drawing behind bars)
            WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

            // 2) Clear legacy flags that push content under bars
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            @SuppressWarnings("deprecation")
            int flags = getWindow().getDecorView().getSystemUiVisibility();
            flags = flags & ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            flags = flags & ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(flags);

            // 3) Ensure an opaque status bar color (not transparent)
            int sbColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            getWindow().setStatusBarColor(sbColor);
        }
    }

    // Apply right after the view is set
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        forceBelowStatusBar();
        applyTopInsetPaddingFallbackIfNeeded();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        forceBelowStatusBar();
        applyTopInsetPaddingFallbackIfNeeded();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        forceBelowStatusBar();
        applyTopInsetPaddingFallbackIfNeeded();
    }

    // Apply again once the activity is fully resumed (wins over late changes)
    @Override
    protected void onPostResume() {
        super.onPostResume();
        forceBelowStatusBar();
    }

    /**
     * Fallback for stubborn cases:
     * If something still makes content go under the status bar on SDK 35,
     * we manually add padding equal to status + navigation bar insets
     * on the root content view.
     */
    private void applyTopInsetPaddingFallbackIfNeeded() {
        if (Build.VERSION.SDK_INT >= 35) {
            final View root = findViewById(android.R.id.content);
            if (root == null) return;

            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets sysBars = insets.getInsets(
                        WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars()
                );

                v.setPadding(
                        v.getPaddingLeft() + sysBars.left,
                        sysBars.top,
                        v.getPaddingRight() + sysBars.right,
                        sysBars.bottom
                );
                return insets;
            });

            ViewCompat.requestApplyInsets(root);
        }
    }


}