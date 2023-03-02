package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CATEGORY_NAME;
import static app.preplotus.utilities.Constants.CAT_TOPIC_NAME;
import static app.preplotus.utilities.Constants.SUBSCRIBED;
import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import app.preplotus.R;
import app.preplotus.adapters.CategoryAdapter;
import app.preplotus.model.CategoryData;
import app.preplotus.model.CategoryDataResponse;
import app.preplotus.model.GeneralResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.btnProceed)
    AppCompatButton btnProceed;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String id;
    private boolean isEnabled = false;
    private String catName, catId, topicName, catTopic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = CategoryActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        id = getIntent().getStringExtra("id");
        topicName = getIntent().getStringExtra("topicName");
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        rvCategories.setLayoutManager(manager);
        if (Utils.isNetworkAvailableShowToast(mContext)) {
            getCategories();
        }
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void getCategories() {
        if (!pd.isShowing()) {
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("group_id", id);

        apiInterface.apiGetCategories(params).enqueue(new Callback<CategoryDataResponse>() {
            @Override
            public void onResponse(Call<CategoryDataResponse> call, Response<CategoryDataResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        CategoryDataResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            int ppp = -1;
                            ArrayList<CategoryData> list = callback.getData();
//                            if (MainActivity.listSelCats!=null && MainActivity.listSelCats.size()>0){
//                                for (int i = 0; i < list.size(); i++) {
//                                    if (MainActivity.listSelCats.contains(list.get(i).getCatId())) {
//                                        ppp = i;
//                                        break;
//                                    }
//                                }
//                            }
                            CategoryAdapter adapter = new CategoryAdapter(mContext, list, ppp);
                            rvCategories.setAdapter(adapter);
//                            if (ppp!=-1){
//                                isEnabled = true;
//                                btnProceed.setAlpha((float) 1.0);
//                            }
                            btnProceed.setVisibility(View.VISIBLE);
                            btnProceed.setAlpha((float) 0.3);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CategoryDataResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

    public void enableProceed(final String catName, final String catId, final String topicName) {
        this.catName = catName;
        this.catId = catId;
        this.catTopic = topicName;
        isEnabled = true;
        btnProceed.setAlpha((float) 1.0);
    }

    @OnClick(R.id.btnProceed)
    public void onProceed() {
        if (isEnabled) {

            if (!pd.isShowing()) {
                pd.show();
            }
            Map<String, String> params = new HashMap<>();

            params.put("userid", Utils.getPrefData(USER_ID, mContext));
            params.put("examid", catId);

            apiInterface.addExamPreferences(params).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    if (pd.isShowing()) {
                        pd.cancel();
                    }
                    try {
                        if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                            GeneralResponse callback = response.body();
//                            Utils.showToast(mContext, callback.getMessage());
                            if (callback.getStatus().equals("success")) {
                                Utils.setPrefData(CATEGORY_NAME, catName, mContext);
                                Utils.setPrefData(CATEGORY_ID, catId, mContext);
                                Utils.setPrefData(SUPER_GROUP_ID, id, mContext);
                                try {
                                    FirebaseMessaging.getInstance().subscribeToTopic(topicName);
                                    FirebaseMessaging.getInstance().subscribeToTopic(catTopic);
                                    Utils.insertTopic(mContext,topicName);
                                    Utils.insertTopic(mContext,catTopic);
                                    if (Utils.getPrefData(CAT_TOPIC_NAME, mContext).trim().length() != 0) {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Utils.getPrefData(CAT_TOPIC_NAME, mContext));
                                        Utils.removeTopic(mContext,Utils.getPrefData(CAT_TOPIC_NAME, mContext));
                                    }
                                    Utils.setPrefData(CAT_TOPIC_NAME, catTopic, mContext);

                                } catch (Exception e) {
                                }
                                Utils.setPrefData("isDetailFetched", "", mContext);
                                Utils.setPrefData(SUBSCRIBED, "", mContext);
                                Intent intent = new Intent(mContext, MainActivity.class);
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
    }

}
