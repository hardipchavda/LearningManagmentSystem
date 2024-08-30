package app.preplotus.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import app.preplotus.R;
import app.preplotus.databinding.ActivityNotesBinding;
import app.preplotus.databinding.ActivityResetPasswordBinding;
import app.preplotus.model.GeneralResponse;
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

public class ResetPasswordActivity extends AppCompatActivity {


    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String otp,email;

    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = ResetPasswordActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        otp = getIntent().getStringExtra("otp");
        email = getIntent().getStringExtra("email");


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

            if (Utils.isNullE(binding.etOTP)) {
                Utils.showToast(mContext, getResources().getString(R.string.otp_msg));
            } else if ((Utils.valE(binding.etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else if (!Utils.valE(binding.etPassword).equals(Utils.valE(binding.etConfirmPassword))) {
                Utils.showToast(mContext, getResources().getString(R.string.password_match_msg));
            } else if (!Utils.valE(binding.etOTP).equals(otp)){
                Utils.showToast(mContext, getResources().getString(R.string.valid_otp_msg));
            } else {
                callResetPasswordApi();
            }

        }
    }

    private void callResetPasswordApi() {
        if (!pd.isShowing()){
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("password", Utils.valE(binding.etPassword));

        apiInterface.apiResetPassword(params).enqueue(new Callback<GeneralResponse>() {
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
                            Intent intent= new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
