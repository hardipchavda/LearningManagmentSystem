package app.preplotus.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import app.preplotus.R;
import app.preplotus.model.ForgotPasswordResponse;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = ForgotPasswordActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnProceed)
    public void onProceed() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {
            if (Utils.isNullE(etEmail)) {
                Utils.showToast(mContext, getResources().getString(R.string.email_msg));
            } else if (!Utils.isValidEmail(Utils.valE(etEmail))) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_email_msg));
            } else {
                callForgotPasswordAPI();
            }
        }

    }

    private void callForgotPasswordAPI() {
        if (!pd.isShowing()){
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("email", Utils.valE(etEmail));

        apiInterface.apiForgortPassword(params).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (pd.isShowing()){
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        ForgotPasswordResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success")) {
                            Intent in = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                            in.putExtra("otp", callback.getCode());
                            in.putExtra("userid", callback.getUserId());
                            in.putExtra("email", Utils.valE(etEmail));
                            startActivity(in);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                if (pd.isShowing()){
                    pd.cancel();
                }
            }
        });

    }

}
