package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import java.util.HashMap;
import java.util.Map;

import app.preplotus.adapters.ViewAdapter;
import app.preplotus.model.PackagesResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllActivity extends AppCompatActivity {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void init() {

        mContext = ViewAllActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        tvTitle.setText(getResources().getString(R.string.mock_test));
        tvTitle.setSelected(true);
        rvNotes.setLayoutManager(new GridLayoutManager(mContext, 2));

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchAllPackages();
        }
    }

    private void fetchAllPackages() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));

        apiInterface.fetchAllPackages(params).enqueue(new Callback<PackagesResponse>() {
            @Override
            public void onResponse(Call<PackagesResponse> call, Response<PackagesResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        PackagesResponse callback = response.body();

                        ViewAdapter adapter = new ViewAdapter(mContext, callback.getData());
                        rvNotes.setAdapter(adapter);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<PackagesResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }

}
