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
import app.preplotus.model.ScoreboardData;
import app.preplotus.model.ScorecardResponse;
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

public class ScorecardFragment extends Fragment {

    @BindView(R.id.tvTotalMarks)
    AppCompatTextView tvTotalMarks;
    @BindView(R.id.tvPercentage)
    AppCompatTextView tvPercentage;
    @BindView(R.id.tvRank)
    AppCompatTextView tvRank;
    @BindView(R.id.tvQueAttempted)
    AppCompatTextView tvQueAttempted;
    @BindView(R.id.tvQueLeft)
    AppCompatTextView tvQueLeft;
    @BindView(R.id.tvQueCorrect)
    AppCompatTextView tvQueCorrect;
    @BindView(R.id.tvQueIncorrect)
    AppCompatTextView tvQueIncorrect;
    @BindView(R.id.tvCorrectMarks)
    AppCompatTextView tvCorrectMarks;
    @BindView(R.id.tvNegativeMarks)
    AppCompatTextView tvNegativeMarks;
    @BindView(R.id.tvTimeTaken)
    AppCompatTextView tvTimeTaken;
    @BindView(R.id.rvLeaderboard)
    RecyclerView rvLeaderboard;
    @BindView(R.id.llLeaderboard)
    LinearLayout llLeaderboard;
    @BindView(R.id.viewRank)
    View viewRank;
    @BindView(R.id.rlRank)
    RelativeLayout rlRank;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scorecard, null);
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
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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
                                tvTotalMarks.setText(data.getGotMarks() + "/" + data.getTotalmarks());
                                tvRank.setText(data.getGotRank() + "/" + data.getTotalrank());
                                tvPercentage.setText(data.getPercentage() + "%");
                                tvQueAttempted.setText(data.getAttempted_que());
                                tvQueLeft.setText(data.getLeft_que());
                                tvQueCorrect.setText(data.getCorrect_que());
                                tvQueIncorrect.setText(data.getIncorrect_que());
                                tvCorrectMarks.setText(data.getCorrect_marks());
                                tvNegativeMarks.setText(data.getNegative_marks());
                                tvTimeTaken.setText(data.getTotal_time_taken() + " min");
                                if (!((ResultsActivity) mContext).getType().equals("practice")) {
                                    rvLeaderboard.setAdapter(new LeaderBoardAdapter(mContext, callback.getLeaderBoardData()));
                                } else {
                                    rlRank.setVisibility(View.GONE);
                                    llLeaderboard.setVisibility(View.GONE);
                                    viewRank.setVisibility(View.GONE);
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