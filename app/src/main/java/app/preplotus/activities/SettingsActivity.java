package app.preplotus.activities;

import static app.preplotus.utilities.Constants.NOTI_FLAG;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import app.preplotus.R;
import app.preplotus.databinding.ActivityNotesBinding;
import app.preplotus.databinding.ActivitySettingsBinding;
import app.preplotus.model.GeneralResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = SettingsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        if (Utils.getPrefData(NOTI_FLAG, mContext).trim().length() == 0 || Utils.getPrefData(NOTI_FLAG, mContext).equals("1")) {
            binding.switchNotification.setChecked(true);
        } else {
            binding.switchNotification.setChecked(false);
        }

        binding.switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
//                    submitNotification("On");
                    Utils.subscribeToTopics(mContext);
                } else {
//                    submitNotification("Off");
                    Utils.unsubscribeFromTopics(mContext);
                }
            }
        });

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.rlChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangePassword();
            }
        });

        binding.rlSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubscription();
            }
        });

    }

    private void submitNotification(final String status) {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("flag", status);

        apiInterface.apiNotification(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success")) {
                            if (status.equals("On")) {
                                Utils.setPrefData(NOTI_FLAG, "1", mContext);
                            } else {
                                Utils.setPrefData(NOTI_FLAG, "0", mContext);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }



    public void onChangePassword() {
        Intent in = new Intent(mContext, ChangePasswordActivity.class);
        startActivity(in);
    }


    public void onSubscription() {
        Intent in = new Intent(mContext, MySubscriptionsActivity.class);
        startActivity(in);
    }

}
