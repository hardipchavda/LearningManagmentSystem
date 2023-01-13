package com.icss.learningmanagmentsystem;

import static com.icss.learningmanagmentsystem.utilities.Constants.ABOUT_US;
import static com.icss.learningmanagmentsystem.utilities.Constants.APP_DETAILS_FETCH_TIME;
import static com.icss.learningmanagmentsystem.utilities.Constants.FACEBOOK_URL;
import static com.icss.learningmanagmentsystem.utilities.Constants.INSTAGRAM_URL;
import static com.icss.learningmanagmentsystem.utilities.Constants.PRIVACY_POLICY_URL;
import static com.icss.learningmanagmentsystem.utilities.Constants.TELEGRAM_URL;
import static com.icss.learningmanagmentsystem.utilities.Constants.TERMS_CONDITION_URL;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_GUIDE;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_ID;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.icss.learningmanagmentsystem.network.APIClient;
import com.icss.learningmanagmentsystem.network.APIInterface;
import com.icss.learningmanagmentsystem.utilities.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        fetchAppDetails(getApplicationContext());
    }

    private void fetchAppDetails(Context mContext) {

        boolean iscall = false;
        Log.e("ttt", "called" );
        if (Utils.getPrefData(APP_DETAILS_FETCH_TIME, mContext).trim().length() == 0) {
            iscall = true;
        } else {
            long time = System.currentTimeMillis() - Long.parseLong(Utils.getPrefData(APP_DETAILS_FETCH_TIME, mContext));
            int hour = (int) TimeUnit.MILLISECONDS.toHours(time);
            Log.e("ttt", "called" + hour);
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
                            Utils.setPrefData(APP_DETAILS_FETCH_TIME, "" + System.currentTimeMillis(), mContext);
                            Log.e("ttt", "called");
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

}
