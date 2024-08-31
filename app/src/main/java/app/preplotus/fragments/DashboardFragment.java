package app.preplotus.fragments;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CAT_TOPIC_NAME;
import static app.preplotus.utilities.Constants.FACEBOOK_URL;
import static app.preplotus.utilities.Constants.INSTAGRAM_URL;
import static app.preplotus.utilities.Constants.SUBSCRIBED;
import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;
import static app.preplotus.utilities.Constants.TELEGRAM_URL;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import app.preplotus.R;
import app.preplotus.activities.ContentActivity;
import app.preplotus.activities.NotesActivity;
import app.preplotus.activities.ReferEarnActivity;
import app.preplotus.adapters.TestsAdapter;
import app.preplotus.databinding.ActivityContentBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.databinding.FragmentDashboardBinding;
import app.preplotus.model.CategoryData;
import app.preplotus.model.DashboardResponse;
import app.preplotus.model.MyExamPreferencesResponse;
import app.preplotus.model.SubscriptionData;
import app.preplotus.model.SubscriptionResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.adapters.NotesAdapter;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    public ArrayList<String> superGroupTopics, superGroupNames;

    private Context mContext;

    private APIInterface apiInterface;
    private ProgressDialog pd;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());

//        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        ButterKnife.bind(this, root);
        init();
        return binding.getRoot();
    }

    private void init() {
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        binding.llViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NotesActivity.class);
                startActivity(intent);
            }
        });

        binding.cvInviteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ReferEarnActivity.class);
                startActivity(intent);
            }
        });

        binding.cvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NotesActivity.class);
                startActivity(intent);
            }
        });

        binding.card2Tests.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                Intent intent = new Intent("change");
                intent.putExtra("stat", "1");
                getActivity().sendBroadcast(intent);

            }
        });

        binding.rvNotes.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.rvExam.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        binding.subplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                startActivity(intent);

            }
        });
        binding.llUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("type", "userguide");
                startActivity(intent);
            }
        });

        binding.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Intent intent = new Intent("change");
                intent.putExtra("stat", "1");
                getActivity().sendBroadcast(intent);
            }
        });

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            if (Utils.isNetworkAvailableShowToast(mContext)) {
                if (Utils.getPrefData("isDetailFetched", mContext).equals("true")) {
                    if (Utils.getPrefData("DetailFetched", mContext).trim().length() == 0) {

                        fetchSuperGroups();
                    } else {
                        long mills = System.currentTimeMillis() - Long.parseLong(Utils.getPrefData("DetailFetched", mContext));
                        int hours = (int) TimeUnit.MILLISECONDS.toHours(mills);
                        if (hours >= 24) {
                            fetchSuperGroups();
                        } else {
                            fetchDashboardData();
                        }
                    }
                } else {
                    fetchSuperGroups();
                }
            }
        }

        binding.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFbAccount();
            }
        });

        binding.btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstaAccount();
            }
        });

        binding.btnTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTeleAccount();
            }
        });

        binding.imgRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRateUs();
            }
        });

    }

    public void fetchSuperGroups() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.getMyExamPrefereces(params).enqueue(new Callback<MyExamPreferencesResponse>() {
            @Override
            public void onResponse(Call<MyExamPreferencesResponse> call, Response<MyExamPreferencesResponse> response) {
//                if (pd.isShowing()) {
//                    pd.cancel();
//                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        MyExamPreferencesResponse callback = response.body();

                        ArrayList<CategoryData> list = callback.getData();
                        superGroupTopics = new ArrayList<>();
                        superGroupNames = new ArrayList<>();
                        String selCatId = Utils.getPrefData(CATEGORY_ID, mContext);
                        for (int i = 0; i < list.size(); i++) {
                            CategoryData data = list.get(i);
                            if (data.getCatId().equals(selCatId)) {

                                Utils.setPrefData(SUPER_GROUP_ID, data.getSupergroup_Id(), mContext);
                                Utils.setPrefData(CAT_TOPIC_NAME, data.getFirebase_topic_name(), mContext);
                                FirebaseMessaging.getInstance().subscribeToTopic(data.getFirebase_topic_name());
                                Utils.insertTopic(mContext,data.getFirebase_topic_name());
                            }
                            if (!superGroupTopics.contains(data.getSupergroup_firebase_Name())) {
                                superGroupTopics.add(data.getSupergroup_firebase_Name());
                                FirebaseMessaging.getInstance().subscribeToTopic(data.getSupergroup_firebase_Name());
                                Utils.insertTopic(mContext,data.getSupergroup_firebase_Name());
                            }
                        }
                        fetchSubscribedPlans();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MyExamPreferencesResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });
    }

    public void fetchSubscribedPlans() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.apiFetchSubsctiptionHistory(params).enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {

                if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                    try {
                        SubscriptionResponse callback = response.body();

                        ArrayList<SubscriptionData> list = callback.getData();

                        for (int i = 0; i < list.size(); i++) {
                            SubscriptionData data = list.get(i);

                            if (data.getIs_active().equals("1") && data.getSupergroup_id().equals(Utils.getPrefData(SUPER_GROUP_ID, mContext))) {

                                Utils.setPrefData(SUBSCRIBED, "yes", mContext);
                                break;
                            } else {
                                Utils.setPrefData(SUBSCRIBED, "", mContext);
                            }

                        }
                    } catch (Exception e) {
                    }
                }

                Utils.setPrefData("isDetailFetched", "true", mContext);
                Utils.setPrefData("DetailFetched", "" + System.currentTimeMillis(), mContext);

                fetchDashboardData();

            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                Utils.setPrefData("isDetailFetched", "true", mContext);
                Utils.setPrefData("DetailFetched", "" + System.currentTimeMillis(), mContext);

                fetchDashboardData();
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });
    }

    private void fetchDashboardData() {

        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));

        apiInterface.fetchDashboardData(params).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        DashboardResponse callback = response.body();
                        if (callback.getNotesData()!=null){
                            NotesAdapter notesAdapter = new NotesAdapter(mContext, callback.getNotesData());
                            binding.rvNotes.setAdapter(notesAdapter);
                        }

                        if (callback.getExamData()!=null){
                            TestsAdapter testsAdapter = new TestsAdapter(mContext, callback.getExamData());
                            binding.rvExam.setAdapter(testsAdapter);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }


    public void openFbAccount() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Utils.getPrefData(FACEBOOK_URL, mContext)));
        startActivity(i);
    }


    public void openInstaAccount() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Utils.getPrefData(INSTAGRAM_URL, mContext)));
        startActivity(i);
    }


    public void openTeleAccount() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Utils.getPrefData(TELEGRAM_URL, mContext)));
        startActivity(i);
    }



    public void onRateUs() {
        String url = "https://play.google.com/store/apps/details?id="+mContext.getPackageName();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}