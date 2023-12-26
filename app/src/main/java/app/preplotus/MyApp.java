package app.preplotus;

import static app.preplotus.utilities.Constants.ABOUT_US;
import static app.preplotus.utilities.Constants.APP_DETAILS_FETCH_TIME;
import static app.preplotus.utilities.Constants.CURRENT_APP_VERSION;
import static app.preplotus.utilities.Constants.FACEBOOK_URL;
import static app.preplotus.utilities.Constants.INSTAGRAM_URL;
import static app.preplotus.utilities.Constants.PRIVACY_POLICY_URL;
import static app.preplotus.utilities.Constants.TELEGRAM_URL;
import static app.preplotus.utilities.Constants.TERMS_CONDITION_URL;
import static app.preplotus.utilities.Constants.USER_GUIDE;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;

import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {

    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        fetchAppDetails(getApplicationContext());
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    private void fetchAppDetails(Context mContext) {

        boolean iscall = false;
        if (Utils.getPrefData(APP_DETAILS_FETCH_TIME, mContext).trim().length() == 0) {
            iscall = true;
        } else {
            long time = System.currentTimeMillis() - Long.parseLong(Utils.getPrefData(APP_DETAILS_FETCH_TIME, mContext));
            int hour = (int) TimeUnit.MILLISECONDS.toHours(time);
            if (hour >= 24) {
                iscall = true;
            }
        }

        if (iscall) {

            Map<String, String> params = new HashMap<>();

            params.put("userid", Utils.getPrefData(USER_ID, mContext));

            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            apiInterface.fetchAppDetails(params).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                            JSONObject jo = new JSONObject(response.body().string());
                            JSONObject data = jo.getJSONObject("data");
                            Utils.setPrefData(ABOUT_US, data.optString("AboutUs"), mContext);
                            Utils.setPrefData(USER_GUIDE, data.optString("UserGuide"), mContext);
                            Utils.setPrefData(FACEBOOK_URL, data.optString("Facebook_url"), mContext);
                            Utils.setPrefData(INSTAGRAM_URL, data.optString("Instagram_url"), mContext);
                            Utils.setPrefData(TELEGRAM_URL, data.optString("Telegram_url"), mContext);
                            Utils.setPrefData(PRIVACY_POLICY_URL, data.optString("Privacy_Policy_url"), mContext);
                            Utils.setPrefData(TERMS_CONDITION_URL, data.optString("Terms_condition_url"), mContext);
                            Utils.setPrefData(CURRENT_APP_VERSION, data.optString("Current_App_version"), mContext);
                            Utils.setPrefData(APP_DETAILS_FETCH_TIME, "" + System.currentTimeMillis(), mContext);
                        }
                    } catch (Exception e) {
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }

    private Dialog dialog;
    public void openDialog(Activity activity) {

        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.dialog_force_update, null);

        AppCompatButton btnUpdate = rl.findViewById(R.id.btnUpdate);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id="+getPackageName();
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(rl);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }


}
