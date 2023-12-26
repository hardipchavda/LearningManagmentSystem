package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.etSubject)
    AppCompatEditText etSubject;
    @BindView(R.id.etDescription)
    AppCompatEditText etDescription;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = ContactUsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        etEmail.setText(Utils.getPrefData(USER_EMAIL, mContext));
        etSubject.requestFocus();
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            if (Utils.isNullE(etEmail)) {
                Utils.showToast(mContext, getResources().getString(R.string.email_msg));
            } else if (!Utils.isValidEmail(Utils.valE(etEmail))) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_email_msg));
            } else if (Utils.isNullE(etSubject)) {
                Utils.showToast(mContext, getResources().getString(R.string.subject_msg));
            } else if (Utils.isNullE(etDescription)) {
                Utils.showToast(mContext, getResources().getString(R.string.des_msg));
            } else {
                apiContactUs();
            }
        }

    }

    private void apiContactUs() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("email", Utils.valE(etEmail));
        params.put("subject", Utils.valE(etSubject));
        params.put("description", Utils.valE(etDescription));
        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.apiContactUs(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
//                        Utils.showToast(mContext,callback.getMessage());
                        if (callback.getStatus().equals("success")){
                        new AlertDialog.Builder(mContext).setMessage(mContext.getResources().getString(R.string.contact_us_msg_)).setCancelable(false).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                        }).show();
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

}
