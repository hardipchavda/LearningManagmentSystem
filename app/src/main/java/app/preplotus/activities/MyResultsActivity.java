package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import app.preplotus.R;
import app.preplotus.adapters.MyResultsAdapter;
import app.preplotus.model.MyResultData;
import app.preplotus.model.MyResultsResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyResultsActivity extends AppCompatActivity {

    @BindView(R.id.rvMyResults)
    RecyclerView rvMyResults;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        rvMyResults.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

        mContext = MyResultsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        tabLayout.addTab(tabLayout.newTab().setText("Tests"));
        tabLayout.addTab(tabLayout.newTab().setText("Practice Tests"));

        fetchResults("test");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("Tests")){
                    fetchResults("test");
                } else {
                    fetchResults("practice");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void fetchResults(final String type) {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
//        params.put("userid", "8");
        params.put("type", type);

        apiInterface.fetchMyResults(params).enqueue(new Callback<MyResultsResponse>() {
            @Override
            public void onResponse(Call<MyResultsResponse> call, Response<MyResultsResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        MyResultsResponse callback = response.body();

                        ArrayList<MyResultData> resultData = callback.getFetchResults();
                        ArrayList<MyResultData> list = new ArrayList<>();

                        for (int i =0;i<resultData.size();i++){
                            MyResultData data = resultData.get(i);
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date date = inputFormat.parse(data.getCreated_at());
                                data.setDateTime(date);
                                list.add(data);
                            } catch (Exception e){}

                        }

                        Collections.sort(list, new Comparator<MyResultData>() {
                            public int compare(MyResultData o1, MyResultData o2) {
                                if (o1.getDateTime() == null || o2.getDateTime() == null)
                                    return 0;
                                return o2.getDateTime().compareTo(o1.getDateTime());
                            }
                        });

                        MyResultsAdapter adapter = new MyResultsAdapter(mContext,list ,type);
                        rvMyResults.setAdapter(adapter);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MyResultsResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

}
