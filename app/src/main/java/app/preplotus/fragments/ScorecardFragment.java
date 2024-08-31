package app.preplotus.fragments;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.activities.ResultsActivity;
import app.preplotus.adapters.LeaderBoardAdapter;
import app.preplotus.databinding.ActivityContentBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.databinding.FragmentScorecardBinding;
import app.preplotus.model.ScoreboardData;
import app.preplotus.model.ScorecardResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScorecardFragment extends Fragment {


    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private FragmentScorecardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScorecardBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        View v = inflater.inflate(R.layout.fragment_scorecard, null);
//        ButterKnife.bind(this, v);
        init();
        return binding.getRoot();
    }

    private void init() {
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        binding.rvLeaderboard.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        fetchScorecardData();
    }

    private void fetchScorecardData() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", ((ResultsActivity) mContext).getTestid());
        if (((ResultsActivity) mContext).getType().equals("first")) {
            params.put("isfirst", "1");
        } else if (((ResultsActivity) mContext).getResult_id().length()>0) {
            params.put("result_id", ((ResultsActivity) mContext).getResult_id());
        }

        apiInterface.fetchScorecard(params).enqueue(new Callback<ScorecardResponse>() {
            @Override
            public void onResponse(Call<ScorecardResponse> call, Response<ScorecardResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        ScorecardResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            try {
                                ScoreboardData data = callback.getScoreCarddata();
                                binding.tvTotalMarks.setText(data.getGotMarks() + "/" + data.getTotalmarks());
                                binding.tvRank.setText(data.getGotRank() + "/" + data.getTotalrank());
                                binding.tvPercentage.setText(data.getPercentage() + "%");
                                binding.tvQueAttempted.setText(data.getAttempted_que());
                                binding.tvQueLeft.setText(data.getLeft_que());
                                binding.tvQueCorrect.setText(data.getCorrect_que());
                                binding.tvQueIncorrect.setText(data.getIncorrect_que());
                                binding.tvCorrectMarks.setText(data.getCorrect_marks());
                                binding.tvNegativeMarks.setText(data.getNegative_marks());
                                binding.tvTimeTaken.setText(data.getTotal_time_taken() + " min");
                                if (!((ResultsActivity) mContext).getType().equals("practice")) {
                                    binding.rvLeaderboard.setAdapter(new LeaderBoardAdapter(mContext, callback.getLeaderBoardData()));
                                } else {
                                    binding.rlRank.setVisibility(View.GONE);
                                    binding.llLeaderboard.setVisibility(View.GONE);
                                    binding.viewRank.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ScorecardResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });
    }

}