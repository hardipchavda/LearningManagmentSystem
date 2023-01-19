package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.adapters.SubjectTopicsAdapter;
import app.preplotus.model.SubjectTopicData;
import app.preplotus.model.SubjectTopicsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectTopicsActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;
    @BindView(R.id.tvCount)
    AppCompatTextView tvCount;
    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    private String id, title;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_notes);
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

        mContext = SubjectTopicsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        rvNotes.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        tvTitle.setText(title);

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchTopics();
        }

    }

    private void fetchTopics() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));
        params.put("subjectid", id);

        apiInterface.fetchSubjectTopics(params).enqueue(new Callback<SubjectTopicsResponse>() {
            @Override
            public void onResponse(Call<SubjectTopicsResponse> call, Response<SubjectTopicsResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        SubjectTopicsResponse callback = response.body();
                        ArrayList<SubjectTopicData> list = callback.getData();
                        if (list!=null){
                            String str = "Total Topics = "+ list.size();
                            tvCount.setText(str);
                            SubjectTopicsAdapter adapter = new SubjectTopicsAdapter(mContext, list);
                            rvNotes.setAdapter(adapter);
                        }

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SubjectTopicsResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }


}