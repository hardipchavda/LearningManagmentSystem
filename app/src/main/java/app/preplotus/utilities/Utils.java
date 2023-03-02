package app.preplotus.utilities;

import static app.preplotus.utilities.Constants.NOTI_FLAG;
import static app.preplotus.utilities.Constants.PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import app.preplotus.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void showToast(final Context mContext, final String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNetworkAvailableShowToast(Context context) {
        if (isNetworkAvailable(context)) {
            return true;
        } else {
            showToast(context, context.getResources().getString(R.string.network_msg));
            return false;
        }
    }

    public static boolean checkResponseCode(int code, Context context) {
        if (code == 200) {
            return true;
        } else {
            showToast(context, context.getResources().getString(R.string.something_wrong_msg));
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String valE(AppCompatEditText et) {
        return et.getText().toString().trim();
    }

    public static boolean isNullE(AppCompatEditText et) {
        if (et == null || et.getText() == null || et.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void setPrefData(String prefName, String value, Context context) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(prefName, value);
        editor.commit();
    }

    public static String getPrefData(String prefName, Context context) {
        SharedPreferences pref;
        String prefValue;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefValue = pref.getString(prefName, "");
        return prefValue;
    }

    public static void insertTopic(Context mContext, String topicName) {
        Gson gson = new Gson();
        String json = Utils.getPrefData("firebaseTopics", mContext).trim();
        ArrayList<String> data = new ArrayList<>();
        if (json.trim().length() > 0) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            data = gson.fromJson(json, type);
        }
        if (!data.contains(topicName)) {
            data.add(topicName);
        }
        Gson gsonTwo = new Gson();
        Utils.setPrefData("firebaseTopics", gsonTwo.toJson(data), mContext);
    }

    public static void removeTopic(Context mContext, String topicName) {
        Gson gson = new Gson();
        String json = Utils.getPrefData("firebaseTopics", mContext).trim();
        ArrayList<String> data = new ArrayList<>();
        if (json.trim().length() > 0) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            data = gson.fromJson(json, type);
        }
        if (data.contains(topicName)) {
            data.remove(topicName);
        }
        if (data.size() == 0) {
            Utils.setPrefData("firebaseTopics", "", mContext);
        } else {
            Gson gsonTwo = new Gson();
            Utils.setPrefData("firebaseTopics", gsonTwo.toJson(data), mContext);
        }
    }

    public static void subscribeToTopics(Context mContext) {
        Gson gson = new Gson();
        String json = Utils.getPrefData("firebaseTopics", mContext).trim();
        ArrayList<String> data = new ArrayList<>();
        if (json.trim().length() > 0) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            data = gson.fromJson(json, type);
            for (int i = 0; i < data.size(); i++) {
                FirebaseMessaging.getInstance().subscribeToTopic(data.get(i));
            }
        }
        Utils.setPrefData(NOTI_FLAG, "1", mContext);
        Utils.showToast(mContext, "Successfully UnMuted Notifications.");
    }

    public static void unsubscribeFromTopics(Context mContext) {
        Gson gson = new Gson();
        String json = Utils.getPrefData("firebaseTopics", mContext).trim();
        ArrayList<String> data = new ArrayList<>();
        if (json.trim().length() > 0) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            data = gson.fromJson(json, type);
            for (int i = 0; i < data.size(); i++) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(data.get(i));
            }
        }
        Utils.setPrefData(NOTI_FLAG, "0", mContext);
        Utils.showToast(mContext, "Successfully Muted Notifications.");
    }

}
