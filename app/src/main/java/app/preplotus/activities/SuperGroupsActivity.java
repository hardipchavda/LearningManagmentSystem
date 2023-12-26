package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.adapters.SuperGroupAdapter;
import app.preplotus.model.SuperGroupResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuperGroupsActivity extends AppCompatActivity {

    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = SuperGroupsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        tvTitle.setText(getResources().getString(R.string.select_group));

        rvCategories.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        if (Utils.isNetworkAvailableShowToast(mContext)) {
            getCategories();
        }
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void getCategories() {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.fetchSuperGroups(params).enqueue(new Callback<SuperGroupResponse>() {
            @Override
            public void onResponse(Call<SuperGroupResponse> call, Response<SuperGroupResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        SuperGroupResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            SuperGroupAdapter adapter = new SuperGroupAdapter(mContext, callback.getData());
                            rvCategories.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SuperGroupResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

}
