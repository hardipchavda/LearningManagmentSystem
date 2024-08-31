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
import app.preplotus.databinding.ActivityContentBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.databinding.FragmentSolutionBinding;
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


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolutionFragment extends Fragment {

    //    @BindView(R.id.tvQtitle)
//    AppCompatTextView tvQtitle;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private ArrayList<SolutionData> listSolutions;
    private int selPos = 0;

    private FragmentSolutionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_solution, null);
        binding = FragmentSolutionBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
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
        fetchSolution();

        binding.llPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrev();
            }
        });

        binding.llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNext();
            }
        });
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
            binding.scrollView.scrollTo(0, 0);
        } catch (Exception e) {
        }

        selPos = pos;
        if (selPos == 0) {
            binding.llPrev.setVisibility(GONE);
        } else {
            binding.llPrev.setVisibility(VISIBLE);
        }
        if (selPos == (listSolutions.size() - 1)) {
            binding.llNext.setVisibility(GONE);
        } else {
            binding.llNext.setVisibility(VISIBLE);
        }
        SolutionData data = listSolutions.get(pos);

        binding.tvQno.setText("Question: " + (pos + 1));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tvQtitle.setText("                        " + Html.fromHtml(data.getQue_title().replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            tvQtitle.setText("                        " + Html.fromHtml(data.getQue_title().replaceAll("\n", "<br>")));
//        }

        binding.nestedWebview.loadDataWithBaseURL(null, data.getQue_title(), "text/html", "UTF-8", null);

        binding.tvMaxMarks.setText(data.getMax_marks());
        binding.tvScoredMarks.setText(data.getScored_marks());

        if (data.getQuestion_Image() != null && data.getQuestion_Image().trim().length() > 0) {
            Glide.with(mContext).load(data.getQuestion_Image()).into(binding.imgQuestion);
            binding.imgQuestion.setVisibility(VISIBLE);
        } else {
            binding.imgQuestion.setVisibility(GONE);
        }

        ArrayList<String> listAnswer = data.getAnswer_array();

        try {
            binding.llAnswers.removeAllViews();
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
            binding.llAnswers.addView(ll);
        }

        binding.tvYourAnswer.setText("Option" + data.getUser_selected_id());
        binding.tvCorrectAnswer.setText("Option" + data.getCorrect_answer_id());
        if (!data.getCorrect_answer_id().contains(data.getUser_selected_id())) {
            binding.tvAttempt.setText("Incorrect");
            binding.tvAttempt.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            binding.tvYourAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            if (data.getUser_selected_id().equals("0")) {
                binding.tvAttempt.setText("Unanswered");
                binding.tvYourAnswer.setText("Unanswered");
            }
        } else {
            binding.tvAttempt.setText("Correct");
            binding.tvAttempt.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            binding.tvYourAnswer.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        }

        String solution = data.getSolution();

        try {

            if (solution != null && solution.trim().length() != 0) {
                binding.webViewSolution.setVisibility(VISIBLE);
                binding.webViewSolution.getSettings().setJavaScriptEnabled(true);
                binding.webViewSolution.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                binding.webViewSolution.getSettings().setBuiltInZoomControls(false);
                binding.webViewSolution.getSettings().setLoadWithOverviewMode(false);
                binding.webViewSolution.getSettings().setLoadsImagesAutomatically(true);

                binding.webViewSolution.getSettings().setUseWideViewPort(false);
                binding.webViewSolution.setWebChromeClient(new WebChromeClient());

                binding.webViewSolution.loadDataWithBaseURL(null, solution, null, "UTF-8", null);
            } else {
                binding.webViewSolution.setVisibility(GONE);
            }
        } catch (Exception e) {
        }

    }


    public void onPrev() {
        showQuestion(selPos - 1);
    }


    public void onNext() {
        showQuestion(selPos + 1);
    }

    public void setQuestionsPallete() {
        LinearLayoutManager manager = new GridLayoutManager(mContext, 4);
        binding.rv.setLayoutManager(manager);
        QuestionPalletAdapter adapter = new QuestionPalletAdapter(mContext, SolutionFragment.this, listSolutions);
        binding.rv.setHasFixedSize(true);
        binding.rv.setAdapter(adapter);
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
        binding.tvAnswered.setText("Answered (" + cans + ")");
        binding.tvNotVisited.setText("Not Visited (" + cnotvis + ")");
        binding.tvNotAnswered.setText("Not Answered (" + cnotans + ")");
        binding.tvMarkedForReview.setText("Marked For Review (" + cmarkd + ")");
    }

}