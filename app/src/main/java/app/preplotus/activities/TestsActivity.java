package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import app.preplotus.R;
;

import java.util.HashMap;
import java.util.Map;

import app.preplotus.adapters.TestsAdapter;
import app.preplotus.model.TestsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestsActivity extends AppCompatActivity {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;
    @BindView(R.id.tvCount)
    AppCompatTextView tvCount;
    @BindView(R.id.tvDetails)
    AppCompatTextView tvDetails;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String id,type,title;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
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

    @OnClick(R.id.tvDetails)
    public void openDetails() {
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("type","details");
        intent.putExtra("title",title);
        intent.putExtra("content",details);
        mContext.startActivity(intent);
    }

    private void init() {

        mContext = TestsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");

        tvTitle.setText(title);

        if (type.equals("package")){
            tvDetails.setVisibility(View.VISIBLE);
        }

        rvNotes.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchTests();
        }
    }

    private String details = "";

    private void fetchTests() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));
        params.put("type", type);
        params.put("id", id);

        apiInterface.fetchAllTests(params).enqueue(new Callback<TestsResponse>() {
            @Override
            public void onResponse(Call<TestsResponse> call, Response<TestsResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        TestsResponse callback = response.body();
                        details = callback.getDetails();
                        TestsAdapter adapter = new TestsAdapter(mContext,callback.getExamData());
                        rvNotes.setAdapter(adapter);

                        tvCount.setText("Total Test= "+callback.getExamData().size());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TestsResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }

}
