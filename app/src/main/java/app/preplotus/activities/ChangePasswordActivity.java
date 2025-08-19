package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import app.preplotus.R;
import app.preplotus.databinding.ActivityChangePasswordBinding;
import app.preplotus.model.GeneralResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {


    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = ChangePasswordActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

    }



    public void onSubmit() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {

            if (Utils.isNullE(binding.etOldPassword)) {
                Utils.showToast(mContext, getResources().getString(R.string.old_password_msg));
            } else if ((Utils.valE(binding.etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else if (!Utils.valE(binding.etPassword).equals(Utils.valE(binding.etConfirmPassword))) {
                Utils.showToast(mContext, getResources().getString(R.string.password_match_msg));
            } else {
                callChangePasswordApi();
            }

        }
    }

    private void callChangePasswordApi() {

        if (!pd.isShowing()){
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID,mContext));
        params.put("old_password", Utils.valE(binding.etOldPassword));
        params.put("new_password", Utils.valE(binding.etPassword));

        apiInterface.apiChangePassword(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()){
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body()!=null) {
                        GeneralResponse callback = response.body();
                        Utils.showToast(mContext,callback.getMessage());
                        if (callback.getStatus().equals("success")){
                            finish();
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (pd.isShowing()){
                    pd.cancel();
                }
            }
        });

    }

}
