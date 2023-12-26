package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CATEGORY_NAME;
import static app.preplotus.utilities.Constants.CCAVENUE_API_URL;
import static app.preplotus.utilities.Constants.FCM_TOKEN;
import static app.preplotus.utilities.Constants.PRIVACY_POLICY_URL;
import static app.preplotus.utilities.Constants.REFERRAL_CODE;
import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;
import static app.preplotus.utilities.Constants.TERMS_CONDITION_URL;
import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_IMAGE;
import static app.preplotus.utilities.Constants.USER_NAME;
import static app.preplotus.utilities.Constants.USER_PHONE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import app.preplotus.adapters.WelcomePagerAdapter;
import app.preplotus.R;
import app.preplotus.model.GeneralResponse;
import app.preplotus.model.LoginSignupResponse;
import app.preplotus.model.LoginUserData;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOptionsActivity extends AppCompatActivity {

    @BindView(R.id.vPager)
    ViewPager viewPager;
    @BindView(R.id.tvReferralCode)
    AppCompatTextView tvReferralCode;

    ArrayList<HashMap<String, String>> listItems;
    Handler hnd;
    private int pagerSwipeTime = 3000;
    Runnable rnb = new Runnable() {
        @Override
        public void run() {
            int page = 0;
            if (viewPager.getCurrentItem() != listItems.size() - 1) {
                page = viewPager.getCurrentItem() + 1;
            }
            viewPager.setCurrentItem(page);
            hnd.postDelayed(this, pagerSwipeTime);
        }
    };
    private Context mContext;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 12;
    private CallbackManager callbackManager;
    private boolean isfacebook = false;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private Dialog dialogF;
    private String refercode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = LoginOptionsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        hnd = new Handler();

        listItems = new ArrayList<>();
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("title", getResources().getString(R.string.login_msg_one));
        map1.put("image", "image_pager_one");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("title", getResources().getString(R.string.login_msg_two));
        map2.put("image", "image_pager_two");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("title", getResources().getString(R.string.login_msg_three));
        map3.put("image", "image_pager_three");

        listItems.add(map1);
        listItems.add(map2);
        listItems.add(map3);

        WelcomePagerAdapter adapter = new WelcomePagerAdapter(LoginOptionsActivity.this, listItems);
        viewPager.setAdapter(adapter);

        hnd.postDelayed(rnb, pagerSwipeTime);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Utils.setPrefData(FCM_TOKEN, token, mContext);

                    }
                });

//        getKey();

    }

    private void getKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("app.preplotus", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            hnd.removeCallbacks(rnb);
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {


        Intent in = new Intent(mContext, LoginActivity.class);
        startActivity(in);






    }

    @OnClick(R.id.btnSignUp)
    public void onSignUp() {

        Intent in = new Intent(mContext, SignUpActivity.class);
        if (refercode!=null && refercode.trim().length()>0){
            in.putExtra("refercode",refercode);
        }
        startActivity(in);

    }

    @OnClick(R.id.btnGoogle)
    public void googleLogin() {

//        Intent in = new Intent(mContext,CategoryActivity.class);
//        startActivity(in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btnFacebook)
    public void facebookLogin() {

        isfacebook = true;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        try {
                                            callSocialLoginApi(object.optString("name"), object.optString("email"), object.optString("id"), "facebook");

                                        } catch (Exception e) {
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                }
        );

        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile")
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            if (isfacebook) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            callSocialLoginApi(account.getDisplayName(), account.getEmail(), account.getId(), "google");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void callSocialLoginApi(String name, String email, String id, String type) {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("email", email);
        params.put("id", id);
        if (Utils.getPrefData(FCM_TOKEN, mContext) != null && Utils.getPrefData(FCM_TOKEN, mContext).trim().length() > 0) {
            params.put("deviceid", Utils.getPrefData(FCM_TOKEN, mContext));
        } else {
            params.put("deviceid", "ASDFGHJKL");


        }
        params.put("type", type);
        if (refercode!=null && refercode.trim().length()>0){
            params.put("referel_code", refercode);
        }

        apiInterface.apiSocialLogin(params).enqueue(new Callback<LoginSignupResponse>() {
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
                            try {
                                mGoogleSignInClient.signOut();
                            } catch (Exception e) {
                            }
                            LoginUserData data = callback.getData();
                            Utils.setPrefData(USER_ID, data.getUserId(), mContext);
                            Utils.setPrefData(USER_NAME, data.getName(), mContext);
                            Utils.setPrefData(USER_EMAIL, data.getEmail(), mContext);
                       //     Utils.setPrefData(USER_PHONE, data.getPhone(), mContext);
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
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

    @OnClick(R.id.tvReferralCode)
    public void enterReferralCode() {
        LinearLayout rl = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_refer_code, null);

        dialogF = new Dialog(LoginOptionsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogF.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogF.setContentView(rl);
        dialogF.setCancelable(true);
        Window window = dialogF.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        AppCompatTextView tvSkip = rl.findViewById(R.id.tvSkip);
        AppCompatTextView tvSubmit = rl.findViewById(R.id.tvSubmit);
        AppCompatEditText etCode = rl.findViewById(R.id.etCode);
        etCode.setText(refercode);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refercode = "";
                dialogF.cancel();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utils.isNullE(etCode)) {
                    refercode = etCode.getText().toString().trim();
                    dialogF.cancel();
                    apiIsValidReferral();
                } else {
                    refercode = "";
                    dialogF.cancel();
                }
            }
        });

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialogF.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogF.show();
    }

    private void apiIsValidReferral() {

        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("referral_code", refercode);

        apiInterface.checkReferralCode(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success") && callback.getCode_status().equals("1")) {
                            tvReferralCode.setText(refercode+" Applied");
                        } else {
                            refercode = "";
                        }
                    } else {
                        refercode = "";
                    }
                } catch (Exception e) {
                    refercode = "";
                }

            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                refercode = "";
            }
        });


    }

    @OnClick(R.id.tvTerms)
    public void onTermsClick(){
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("type","url");
        intent.putExtra("title",getResources().getString(R.string.terms_condi));
        intent.putExtra("content",Utils.getPrefData(TERMS_CONDITION_URL,mContext));
        startActivity(intent);
    }

    @OnClick(R.id.tvPrivacy)
    public void onPrivacyClick(){
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("type","url");
        intent.putExtra("title",getResources().getString(R.string.privacy_policy));
        intent.putExtra("content",Utils.getPrefData(PRIVACY_POLICY_URL,mContext));
        startActivity(intent);
    }

}
