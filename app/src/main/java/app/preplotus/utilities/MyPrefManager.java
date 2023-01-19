package app.preplotus.utilities;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class MyPrefManager {

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
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String PASSWORD = "password";
    private static final String ID = "id";


    public MyPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void ragister(String Id, String Name,String Mobile,String Email,String Password) {
        editor.putString(ID,Id);
        editor.putString(NAME,Name);
        editor.putString(EMAIL,Email);
        editor.putString(PASSWORD,Password);
        editor.putString(MOBILE,Mobile);
        editor.commit();
    }

    public HashMap<String, String> getRagister() {
        HashMap<String, String> data = new HashMap<>();
        data.put(ID, pref.getString(ID, null));
        data.put(NAME, pref.getString(NAME, null));
        data.put(EMAIL, pref.getString(EMAIL, null));
        data.put(MOBILE, pref.getString(MOBILE, null));
        data.put(PASSWORD, pref.getString(PASSWORD, null));
        return data;

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(String.valueOf(pref), Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID, null) != null;
    }



}
