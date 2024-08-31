package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.adapters.SubjectTopicsAdapter;
import app.preplotus.databinding.ActivityNotesBinding;
import app.preplotus.databinding.ActivitySubjectNotesBinding;
import app.preplotus.model.SubjectTopicData;
import app.preplotus.model.SubjectTopicsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectTopicsActivity extends AppCompatActivity {


    private String id, title;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String from;

    private ActivitySubjectNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
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


    private void init() {

        mContext = SubjectTopicsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        binding.rvNotes.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        binding.tvTitle.setText(title);

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchTopics();
        }

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
                            binding.tvCount.setText(str);
                            SubjectTopicsAdapter adapter = new SubjectTopicsAdapter(mContext, list);
                            binding.rvNotes.setAdapter(adapter);
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