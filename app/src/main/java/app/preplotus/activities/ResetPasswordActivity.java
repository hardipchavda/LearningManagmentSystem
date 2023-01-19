package app.preplotus.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import app.preplotus.R;
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

    @BindView(R.id.etOTP)
    AppCompatEditText etOTP;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    AppCompatEditText etConfirmPassword;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String otp,email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
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

    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {

            if (Utils.isNullE(etOTP)) {
                Utils.showToast(mContext, getResources().getString(R.string.otp_msg));
            } else if ((Utils.valE(etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else if (!Utils.valE(etPassword).equals(Utils.valE(etConfirmPassword))) {
                Utils.showToast(mContext, getResources().getString(R.string.password_match_msg));
            } else if (!Utils.valE(etOTP).equals(otp)){
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
        params.put("password", Utils.valE(etPassword));

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
