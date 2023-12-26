package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CATEGORY_NAME;
import static app.preplotus.utilities.Constants.FCM_TOKEN;
import static app.preplotus.utilities.Constants.REFERRAL_CODE;
import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_IMAGE;
import static app.preplotus.utilities.Constants.USER_NAME;
import static app.preplotus.utilities.Constants.USER_PHONE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import app.preplotus.R;
import app.preplotus.model.LoginSignupResponse;
import app.preplotus.model.LoginUserData;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = LoginActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
    }

    @OnClick(R.id.tvForgotPassword)
    public void onForgotPassword() {
        Intent in = new Intent(mContext, ForgotPasswordActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {
            if (Utils.isNullE(etEmail)) {
                Utils.showToast(mContext, getResources().getString(R.string.email_msg));
            } else if (!Utils.isValidEmail(Utils.valE(etEmail))) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_email_msg));
            } else if ((Utils.valE(etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else {
                callLoginApi();
            }
        }
    }

    private void callLoginApi() {
        if (!pd.isShowing()){
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("email", Utils.valE(etEmail));
        params.put("password", Utils.valE(etPassword));
        params.put("deviceid", Utils.getPrefData(FCM_TOKEN, mContext));

        apiInterface.apiLogin(params).enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if (pd.isShowing()){
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        LoginSignupResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success")) {
                            LoginUserData data = callback.getData();

                            Utils.setPrefData(USER_ID, data.getUserId(), mContext);
                            Utils.setPrefData(USER_NAME, data.getName(), mContext);
                            Utils.setPrefData(USER_EMAIL, data.getEmail(), mContext);
                            Utils.setPrefData(USER_PHONE, data.getPhone(), mContext);

                            Utils.setPrefData(USER_IMAGE, data.getUserImage(), mContext);
                            Utils.setPrefData(CATEGORY_ID, data.getExam_id(), mContext);
                            Utils.setPrefData(CATEGORY_NAME, data.getExam_name(), mContext);
                            Utils.setPrefData(REFERRAL_CODE, data.getUsercode(), mContext);
                            if (data.getExam_id()!=null && data.getExam_id().trim().length()>0){
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);



                            } else {

                                Intent intent = new Intent(mContext, SuperGroupsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                if (pd.isShowing()){
                    pd.cancel();
                }
            }
        });
    }

}
