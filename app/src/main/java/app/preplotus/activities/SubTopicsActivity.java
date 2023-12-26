package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import app.preplotus.adapters.SubTopicsAdapter;
import app.preplotus.model.SubTopicsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import app.preplotus.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTopicsActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;
    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    private String topicId, topicTitle;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        init();
    }


    @Override
    public void onBackPressed() {
        if (from!=null && from.equals("notification")){
            Intent in = new Intent(mContext,MainActivity.class);
            startActivity(in);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void init() {

        mContext = SubTopicsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        rvNotes.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        topicId = getIntent().getStringExtra("topicId");
        topicTitle = getIntent().getStringExtra("topicTitle");

        tvTitle.setText(topicTitle);
        tvTitle.setSelected(true);
        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchSubTopics();
        }

    }

    private void fetchSubTopics() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));
        params.put("topicid", topicId);

        apiInterface.fetchSubTopics(params).enqueue(new Callback<SubTopicsResponse>() {
            @Override
            public void onResponse(Call<SubTopicsResponse> call, Response<SubTopicsResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        SubTopicsResponse callback = response.body();

                        SubTopicsAdapter adapter = new SubTopicsAdapter(mContext, callback.getData());
                        rvNotes.setAdapter(adapter);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SubTopicsResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }


}