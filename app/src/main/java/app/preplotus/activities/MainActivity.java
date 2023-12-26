package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CATEGORY_NAME;
import static app.preplotus.utilities.Constants.CURRENT_APP_VERSION;
import static app.preplotus.utilities.Constants.GENERAL_TOPIC;
import static app.preplotus.utilities.Constants.SUBSCRIBED;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_IMAGE;
import static app.preplotus.utilities.Constants.USER_NAME;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.messaging.FirebaseMessaging;

import app.preplotus.MyApp;
import app.preplotus.R;
import app.preplotus.fragments.DashboardFragment;
import app.preplotus.fragments.HomeFragment;
import app.preplotus.fragments.NotificationsFragment;
import app.preplotus.model.ExamData;
import app.preplotus.model.GeneralResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.CategoryDialog;
import app.preplotus.utilities.Utils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    public final static int ID_VENDORS = 2;
    private final static int ID_HOME = 1;
    private final static int ID_SHOP = 3;

    Toolbar toolbar;
    TextView toolbarText;
    MeowBottomNavigation bottomNavigation;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getStringExtra("stat").equals("1")) {
                    bottomNavigation.show(ID_VENDORS, true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragment = new HomeFragment();
                            FragmentManager manager = getSupportFragmentManager();
                           if (!manager.isDestroyed()){
                               FragmentTransaction transaction = manager.beginTransaction();
                               transaction.replace(R.id.container, fragment);
                               transaction.commit();
                           }
                        }
                    },500);
                }
            }
        }
    };
    CircleImageView imgUser;
    AppCompatTextView tvUserName;
//    AppCompatTextView tvLogin;
//    AppCompatTextView tvCategory;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private DrawerLayout drawer;
    public static ArrayList<String> listSelCats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Utils.setPrefData(USER_ID,"33", MainActivity.this);
        toolbarText = findViewById(R.id.txt);
//        tvLogin = findViewById(R.id.tvLogin);
//        tvCategory = findViewById(R.id.tvCategory);
        init();
        registerReceiver(broadcastReceiver, new IntentFilter("change"));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DashboardFragment()).commit();
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_VENDORS, R.drawable.ic_resulte));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_SHOP, R.drawable.ic_outline_contacts_24));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                Fragment selected = null;

                switch (item.getId()) {
                    case ID_HOME:
                        selected = new DashboardFragment();
                        break;
                    case ID_VENDORS:
                        selected = new HomeFragment();
                        break;
                    case ID_SHOP:
                        selected = new NotificationsFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();

            }

        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                switch (item.getId()) {
                    case ID_HOME:
                        break;
                    case ID_VENDORS:
                        break;
                    case ID_SHOP:
                        break;
                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.show(ID_HOME, true);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        toolbarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
//                startActivity(intent);
                CategoryDialog categoryDialog = new CategoryDialog(mContext);
                categoryDialog.show(getSupportFragmentManager(),"category");
            }
        });


        toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        //  toolbar.setTitleTextColor(Color.RED);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        imgUser = findViewById(R.id.imgUser);
        tvUserName = findViewById(R.id.tvUserName);
        setUserData();

        if ( getIntent().getStringExtra("redirect_type")!=null && getIntent().getStringExtra("redirect_type").equals("alltest")) {
            Intent intent = new Intent("change");
            intent.putExtra("stat", "1");
            sendBroadcast(intent);
        }

    }


    private void init() {
        mContext = MainActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        toolbarText.setText(Utils.getPrefData(CATEGORY_NAME, mContext));
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        FirebaseMessaging.getInstance().subscribeToTopic(GENERAL_TOPIC);
        Utils.insertTopic(mContext,GENERAL_TOPIC);
//        tvLogin.setText("User: "+Utils.getPrefData(USER_ID, mContext));
//        tvCategory.setText("Category: "+Utils.getPrefData(CATEGORY_ID, mContext));
        checkForceUpdate();
   }

    private void checkForceUpdate(){

        try {

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if (Utils.getPrefData(CURRENT_APP_VERSION, mContext).trim().length()>0 && !version.equals(Utils.getPrefData(CURRENT_APP_VERSION, mContext))){
                Handler hnd = new Handler();
                hnd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyApp.getInstance().openDialog(MainActivity.this);
                    }
                },1000);
            }

        } catch (Exception e){}
    }

    public void setUserData() {
        try {
            tvUserName.setText(Utils.getPrefData(USER_NAME, mContext));
            if (Utils.getPrefData(USER_IMAGE, mContext) != null && Utils.getPrefData(USER_IMAGE, mContext).trim().length() > 0) {
                Glide.with(mContext).load(Utils.getPrefData(USER_IMAGE, mContext)).placeholder(R.drawable.profile).into(imgUser);
            }
        } catch (Exception e) {
        }
    }

    private void logoutDialog() {

        new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.logout))
                .setMessage(getResources().getString(R.string.logout_msg))
                .setNegativeButton(getResources().getString(R.string.no), null)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Utils.isNetworkAvailableShowToast(mContext)) {
                            dialogInterface.cancel();
                            apiLogout();
                        }
                    }
                }).show();

    }

    private void apiLogout() {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.apiLogout(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success")) {
                            Utils.setPrefData(SUBSCRIBED,"",mContext);
                            Utils.setPrefData("isDetailFetched","",mContext);
                            Utils.setPrefData(USER_ID, "", mContext);
                            Utils.setPrefData(CATEGORY_ID, "", mContext);
                            Intent intent = new Intent(mContext, LoginOptionsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomNavigation.show(ID_HOME, true);
    }

    @OnClick(R.id.llMyResults)
    public void onMyResults() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, MyResultsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llHelpSupport)
    public void onHelpSupport() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, ContactUsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llAbout)
    public void onAbout() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, ContentActivity.class);
        intent.putExtra("type", "aboutus");
        startActivity(intent);
    }

    @OnClick(R.id.llPracticeEarn)
    public void onPracticeEarn() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, PracticeEarnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llReferEarn)
    public void onReferEarn() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, ReferEarnActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llMyCoins)
    public void onMyCoins() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(mContext, MyCoinsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llLogout)
    public void onLogout() {
        drawer.closeDrawer(GravityCompat.START);
        logoutDialog();
    }

    @OnClick(R.id.llHome)
    public void onHome() {
        drawer.closeDrawer(GravityCompat.START);
        bottomNavigation.show(ID_HOME, true);
        Fragment fragment = new DashboardFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
