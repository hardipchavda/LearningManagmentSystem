package app.preplotus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import app.preplotus.adapters.ResultAdapter;
import app.preplotus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends AppCompatActivity {

    public String testid, title, type = "", result_id = "";
    ResultAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        ButterKnife.bind(this);
        init();
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new ResultAdapter(type, this.getSupportFragmentManager(), 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getType().equals("first")) {
            super.onBackPressed();
        } else if (getResult_id().length() > 0) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void init() {
        testid = getIntent().getStringExtra("testid");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        result_id = getIntent().getStringExtra("id");
        tvTitle.setText(title);
    }

    public String getTestid() {
        return testid;
    }

    public String getType() {
        if (type == null) {
            return "";
        }
        return type;
    }

    public String getResult_id() {
        if (result_id == null) {
            return "";
        }
        return result_id;
    }


}