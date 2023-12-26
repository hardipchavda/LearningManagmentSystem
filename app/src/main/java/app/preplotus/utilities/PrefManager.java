package app.preplotus.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "CarWahan";

    // All Shared Preferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_PASSWORD = "password";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public void createLogin(String name ,String email,  String profile) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PROFILE, profile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }



    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        profile.put(KEY_NAME, pref.getString(KEY_NAME, null));
        profile.put(KEY_PROFILE, pref.getString(KEY_PROFILE, null));
        return profile;
    }
}
