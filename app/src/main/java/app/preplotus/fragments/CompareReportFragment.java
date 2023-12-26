package app.preplotus.fragments;

import static app.preplotus.utilities.Constants.USER_ID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.preplotus.R;
import app.preplotus.activities.ResultsActivity;
import app.preplotus.adapters.CompareAdapter;
import app.preplotus.model.CompareResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompareReportFragment extends Fragment {

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @BindView(R.id.rvCompare)
    RecyclerView rvCompare;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.compare_fragment, null);
        ButterKnife.bind(this, v);
        init();
        return v;
    }
    private void init() {
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        rvCompare.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rvCompare.setHasFixedSize(true);
        fetchCompareData();
    }
    private void fetchCompareData(){
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", ((ResultsActivity) mContext).getTestid());

        apiInterface.fetchCompareData(params).enqueue(new Callback<CompareResponse>() {
            @Override
            public void onResponse(Call<CompareResponse> call, Response<CompareResponse> response) {
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        CompareResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            CompareAdapter adapter = new CompareAdapter(mContext, callback.getResults());
                            rvCompare.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CompareResponse> call, Throwable t) {

            }
        });
    }
}