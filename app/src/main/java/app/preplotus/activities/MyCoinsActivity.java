package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.adapters.MyCoinsAdapter;
import app.preplotus.databinding.ActivityMyCoinsBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.model.MyCoinsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoinsActivity extends BaseActivity {


    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private ActivityMyCoinsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyCoinsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = MyCoinsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        binding.rvMyCoins.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        getMyCoins();

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void getMyCoins() {

        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
//        params.put("userid", "8");

        apiInterface.getCoinsHistory(params).enqueue(new Callback<MyCoinsResponse>() {
            @Override
            public void onResponse(Call<MyCoinsResponse> call, Response<MyCoinsResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        MyCoinsResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            try {
                                binding.tvCoinBalance.setText(callback.getCoinsBalance());
                                int cnt = Integer.parseInt(callback.getCoinsBalance()) + Integer.parseInt(callback.getUsedCoins());
                                binding.tvEarnings.setText(""+cnt);
                                binding.tvSpentCoins.setText(callback.getUsedCoins());
                                double ir = Double.parseDouble(callback.getCoinsBalance()) / Double.parseDouble(callback.getCoinsValue());
                                binding.tvWorth.setText("Worth ₹ "+((int)ir));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            if (callback.getCoinsHistory()!=null && callback.getCoinsHistory().size()>0) {
                                MyCoinsAdapter adapter = new MyCoinsAdapter(mContext, callback.getCoinsHistory());
                                binding.rvMyCoins.setAdapter(adapter);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MyCoinsResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }

}
