package app.preplotus.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static app.preplotus.utilities.Constants.USER_ID;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import app.preplotus.R;
import app.preplotus.activities.ResultsActivity;
import app.preplotus.adapters.QuestionPalletAdapter;
import app.preplotus.model.SolutionData;
import app.preplotus.model.SolutionResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.NestedWebView;
import app.preplotus.utilities.TouchyWebView;
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

public class SolutionFragment extends Fragment {

    @BindView(R.id.tvQno)
    AppCompatTextView tvQno;
    //    @BindView(R.id.tvQtitle)
//    AppCompatTextView tvQtitle;
    @BindView(R.id.nested_webview)
    NestedWebView webView;
    @BindView(R.id.llAnswers)
    LinearLayout llAnswers;
    @BindView(R.id.tvAttempt)
    AppCompatTextView tvAttempt;
    @BindView(R.id.tvYourAnswer)
    AppCompatTextView tvYourAnswer;
    @BindView(R.id.tvCorrectAnswer)
    AppCompatTextView tvCorrectAnswer;
    @BindView(R.id.tvMaxMarks)
    AppCompatTextView tvMaxMarks;
    @BindView(R.id.tvScoredMarks)
    AppCompatTextView tvScoredMarks;
    @BindView(R.id.webViewSolution)
    TouchyWebView webViewSolution;
    @BindView(R.id.llPrev)
    LinearLayout llPrev;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.llNext)
    LinearLayout llNext;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.imgQuestion)
    ImageView imgQuestion;
    @BindView(R.id.tvNotVisited)
    AppCompatTextView tvNotVisited;
    @BindView(R.id.tvAnswered)
    AppCompatTextView tvAnswered;
    @BindView(R.id.tvNotAnswered)
    AppCompatTextView tvNotAnswered;
    @BindView(R.id.tvMarkedForReview)
    AppCompatTextView tvMarkedForReview;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private ArrayList<SolutionData> listSolutions;
    private int selPos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_solution, null);
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
        fetchSolution();
    }

    private void fetchSolution() {

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", ((ResultsActivity) mContext).getTestid());
        if (((ResultsActivity) mContext).getType().equals("first")) {
            params.put("isfirst", "1");
        } else if (((ResultsActivity) mContext).getResult_id().length() > 0) {
            params.put("result_id", ((ResultsActivity) mContext).getResult_id());
        }


        apiInterface.fetchSolutions(params).enqueue(new Callback<SolutionResponse>() {
            @Override
            public void onResponse(Call<SolutionResponse> call, Response<SolutionResponse> response) {
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        SolutionResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            listSolutions = callback.getFetch_solution();
                            showQuestion(0);
                            setQuestionsPallete();
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<SolutionResponse> call, Throwable t) {

            }
        });

    }

    public void showQuestion(final int pos) {

        try {
            scrollView.scrollTo(0, 0);
        } catch (Exception e) {
        }

        selPos = pos;
        if (selPos == 0) {
            llPrev.setVisibility(GONE);
        } else {
            llPrev.setVisibility(VISIBLE);
        }
        if (selPos == (listSolutions.size() - 1)) {
            llNext.setVisibility(GONE);
        } else {
            llNext.setVisibility(VISIBLE);
        }
        SolutionData data = listSolutions.get(pos);

        tvQno.setText("Question: " + (pos + 1));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tvQtitle.setText("                        " + Html.fromHtml(data.getQue_title().replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            tvQtitle.setText("                        " + Html.fromHtml(data.getQue_title().replaceAll("\n", "<br>")));
//        }

        webView.loadDataWithBaseURL(null, data.getQue_title(), "text/html", "UTF-8", null);

        tvMaxMarks.setText(data.getMax_marks());
        tvScoredMarks.setText(data.getScored_marks());

        if (data.getQuestion_Image() != null && data.getQuestion_Image().trim().length() > 0) {
            Glide.with(mContext).load(data.getQuestion_Image()).into(imgQuestion);
            imgQuestion.setVisibility(VISIBLE);
        } else {
            imgQuestion.setVisibility(GONE);
        }

        ArrayList<String> listAnswer = data.getAnswer_array();

        try {
            llAnswers.removeAllViews();
        } catch (Exception e) {
        }

        ArrayList<String> listImgs = data.getAns_images();

        for (int i = 0; i < listAnswer.size(); i++) {

            final RelativeLayout ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.row_answers, null);
            AppCompatTextView tvAnswer = ll.findViewById(R.id.tvAnswer);
            AppCompatTextView tvNo = ll.findViewById(R.id.tvNo);
            ImageView imgSign = ll.findViewById(R.id.imgSign);
            ImageView imgAnswer = ll.findViewById(R.id.imgAnswer);

            tvNo.setText((i + 1) + ".");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvAnswer.setText(Html.fromHtml(listAnswer.get(i).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvAnswer.setText(Html.fromHtml(listAnswer.get(i).replaceAll("\n", "<br>")));
            }
            try {
                imgSign.clearColorFilter();
            } catch (Exception e) {
            }

            try {
                if (listImgs.get(i) != null && listImgs.get(i).trim().length() > 0) {
                    Glide.with(mContext).load(listImgs.get(i)).into(imgAnswer);
                    imgAnswer.setVisibility(VISIBLE);
                } else {
                    imgAnswer.setVisibility(GONE);
                }
            } catch (Exception e) {
            }

//            if ((i + 1) == Integer.parseInt(listSolutions.get(selPos).getCorrect_answer_id())) {
            if (listSolutions.get(selPos).getCorrect_answer_id().contains("" + (i + 1))) {
                imgSign.setVisibility(VISIBLE);
                imgSign.setImageResource(R.drawable.check_c);
            } else if ((i + 1) == Integer.parseInt(listSolutions.get(selPos).getUser_selected_id())) {
                imgSign.setVisibility(VISIBLE);
                imgSign.setImageResource(R.drawable.cross);
                imgSign.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                imgSign.setVisibility(GONE);
            }
            llAnswers.addView(ll);
        }

        tvYourAnswer.setText("Option" + data.getUser_selected_id());
        tvCorrectAnswer.setText("Option" + data.getCorrect_answer_id());
        if (!data.getCorrect_answer_id().contains(data.getUser_selected_id())) {
            tvAttempt.setText("Incorrect");
            tvAttempt.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            tvYourAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            if (data.getUser_selected_id().equals("0")) {
                tvAttempt.setText("Unanswered");
                tvYourAnswer.setText("Unanswered");
            }
        } else {
            tvAttempt.setText("Correct");
            tvAttempt.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            tvYourAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }

        String solution = data.getSolution();

        try {

            if (solution != null && solution.trim().length() != 0) {
                webViewSolution.setVisibility(VISIBLE);
                webViewSolution.getSettings().setJavaScriptEnabled(true);
                webViewSolution.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webViewSolution.getSettings().setBuiltInZoomControls(false);
                webViewSolution.getSettings().setLoadWithOverviewMode(false);
                webViewSolution.getSettings().setLoadsImagesAutomatically(true);

                webViewSolution.getSettings().setUseWideViewPort(false);
                webViewSolution.setWebChromeClient(new WebChromeClient());

                webViewSolution.loadDataWithBaseURL(null, solution, null, "UTF-8", null);
            } else {
                webViewSolution.setVisibility(GONE);
            }
        } catch (Exception e) {
        }

    }

    @OnClick(R.id.llPrev)
    public void onPrev() {
        showQuestion(selPos - 1);
    }

    @OnClick(R.id.llNext)
    public void onNext() {
        showQuestion(selPos + 1);
    }

    public void setQuestionsPallete() {
        LinearLayoutManager manager = new GridLayoutManager(mContext, 4);
        rv.setLayoutManager(manager);
        QuestionPalletAdapter adapter = new QuestionPalletAdapter(mContext, SolutionFragment.this, listSolutions);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        int cans = 0, cnotans = 0, cnotvis = 0, cmarkd = 0;
        for (int i = 0; i < listSolutions.size(); i++) {
            final SolutionData data = listSolutions.get(i);
            if (data.getIsanswered().equals("1")) {
                cans = cans + 1;
            } else if (data.getMarkedforreview().equals("1")) {
                cmarkd = cmarkd + 1;
            } else if (!data.getIsvisited().equals("1")) {
                cnotvis = cnotvis + 1;
            } else {
                cnotans = cnotans + 1;
            }
        }
        tvAnswered.setText("Answered (" + cans + ")");
        tvNotVisited.setText("Not Visited (" + cnotvis + ")");
        tvNotAnswered.setText("Not Answered (" + cnotans + ")");
        tvMarkedForReview.setText("Marked For Review (" + cmarkd + ")");
    }

}