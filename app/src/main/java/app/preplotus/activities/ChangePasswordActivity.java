package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
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

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.etOldPassword)
    AppCompatEditText etOldPassword;
    @BindView(R.id.etPassword)
    AppCompatEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    AppCompatEditText etConfirmPassword;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = ChangePasswordActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {
        if (Utils.isNetworkAvailableShowToast(mContext)) {

            if (Utils.isNullE(etOldPassword)) {
                Utils.showToast(mContext, getResources().getString(R.string.old_password_msg));
            } else if ((Utils.valE(etPassword)).trim().length() < 6) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_password_msg));
            } else if (!Utils.valE(etPassword).equals(Utils.valE(etConfirmPassword))) {
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
        params.put("old_password", Utils.valE(etOldPassword));
        params.put("new_password", Utils.valE(etPassword));

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
