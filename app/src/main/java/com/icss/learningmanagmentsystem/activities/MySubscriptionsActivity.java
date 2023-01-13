package com.icss.learningmanagmentsystem.activities;

import static com.icss.learningmanagmentsystem.utilities.Constants.CATEGORY_ID;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icss.learningmanagmentsystem.R;
import com.icss.learningmanagmentsystem.adapters.MySubscriptionsAdapter;
import com.icss.learningmanagmentsystem.adapters.SubTopicsAdapter;
import com.icss.learningmanagmentsystem.model.SubTopicsResponse;
import com.icss.learningmanagmentsystem.model.SubscriptionResponse;
import com.icss.learningmanagmentsystem.network.APIClient;
import com.icss.learningmanagmentsystem.network.APIInterface;
import com.icss.learningmanagmentsystem.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySubscriptionsActivity extends AppCompatActivity {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscription);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void init() {
        mContext = MySubscriptionsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        rvNotes.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        fetchSubscriptions();

    }

    private void fetchSubscriptions() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
//        params.put("userid", "10");

        apiInterface.apiFetchSubsctiptionHistory(params).enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {

                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        SubscriptionResponse callback = response.body();

                        MySubscriptionsAdapter adapter = new MySubscriptionsAdapter(mContext, callback.getData());
                        rvNotes.setAdapter(adapter);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

}
