package app.preplotus.activities;

import static app.preplotus.utilities.Constants.FCM_TOKEN;
import static app.preplotus.utilities.Constants.REFERRAL_CODE;
import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_NAME;
import static app.preplotus.utilities.Constants.USER_PHONE;

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
import app.preplotus.databinding.ActivitySignupBinding;
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

public class SignUpActivity extends AppCompatActivity {

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String refercode = "";

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = SignUpActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        refercode = getIntent().getStringExtra("refercode");

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignup();
            }
        });

    }



    public void onSignup() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {

            if (Utils.isNullE(binding.etName)) {
                Utils.showToast(mContext, getResources().getString(R.string.name_msg));
            } else if (Utils.isNullE(binding.etEmail)) {
                Utils.showToast(mContext, getResources().getString(R.string.email_msg));
            } else if (!Utils.isValidEmail(Utils.valE(binding.etEmail))) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_email_msg));
            } else if ((Utils.valE(binding.etPhoneNumber)).trim().length() < 10) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_phone_msg));
            } else if ((Utils.valE(binding.etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else {
                callSignupApi();
            }
        }
    }

    private void callSignupApi() {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("name", Utils.valE(binding.etName));
        params.put("email", Utils.valE(binding.etEmail));
        params.put("phone", Utils.valE(binding.etPhoneNumber));
        params.put("password", Utils.valE(binding.etPassword));
//        params.put("deviceid", Utils.getPrefData(FCM_TOKEN, mContext));
        if (Utils.getPrefData(FCM_TOKEN, mContext) != null && Utils.getPrefData(FCM_TOKEN, mContext).trim().length() > 0) {
            params.put("deviceid", Utils.getPrefData(FCM_TOKEN, mContext));
        } else {
            params.put("deviceid", "ASDFGHJKL");
        }
        if (refercode!=null && refercode.trim().length()>0){
            params.put("referel_code", refercode);
        }
        apiInterface.apiRegister(params).enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if (pd.isShowing()) {
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
//                             Utils.setPrefData(CATEGORY_ID,data.getExam_id(),mContext);
//                            Utils.setPrefData(CATEGORY_NAME,data.getExam_name(),mContext);
                            Utils.setPrefData(REFERRAL_CODE, data.getUsercode(), mContext);
                            Intent intent = new Intent(mContext, SuperGroupsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

}
