package app.preplotus.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import app.preplotus.adapters.QuestionNumberAdapter;
import app.preplotus.model.GeneralResponse;
import app.preplotus.model.QuestionListResponse;
import app.preplotus.model.QuestionsData;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.CustomDialog;
import app.preplotus.R;
import app.preplotus.utilities.NestedWebView;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static androidx.appcompat.widget.Toolbar.*;
import static app.preplotus.utilities.Constants.USER_ID;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity {
    ImageView navbar;

    DrawerLayout drawer;
    Toolbar toolbar;
    RecyclerView rv;
    QuestionNumberAdapter adapter;
//    @BindView(R.id.tvQtitle)
//    AppCompatTextView tvQtitle;

    @BindView(R.id.nested_webview)
    NestedWebView webView;
    @BindView(R.id.tvQno)
    AppCompatTextView tvQno;
    @BindView(R.id.tvTestTitle)
    AppCompatTextView tvTestTitle;
    @BindView(R.id.tvMaxMarks)
    AppCompatTextView tvMaxMarks;
    @BindView(R.id.tvNotVisited)
    AppCompatTextView tvNotVisited;
    @BindView(R.id.tvAnswered)
    AppCompatTextView tvAnswered;
    @BindView(R.id.tvNotAnswered)
    AppCompatTextView tvNotAnswered;
    @BindView(R.id.tvTime)
    AppCompatTextView tvTime;
    @BindView(R.id.tvMarkedForReview)
    AppCompatTextView tvMarkedForReview;
    @BindView(R.id.rgQuestions)
    LinearLayout rgQuestions;
    @BindView(R.id.llClearResponse)
    LinearLayout llClearResponse;
    @BindView(R.id.llPrev)
    LinearLayout llPrev;
    @BindView(R.id.llNext)
    LinearLayout llNext;
    @BindView(R.id.imgMarkedForReview)
    ImageView imgMarkedForReview;
    @BindView(R.id.imgQuestion)
    ImageView imgQuestion;
    private String testid = "", marks, time, title, type;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private ArrayList<QuestionsData> listQuestions;
    private int selPos = 0;
    private CountDownTimer timer;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);
        ButterKnife.bind(this);
        navbar = findViewById(R.id.navbar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.RED);
        drawer = findViewById(R.id.drawer_layout);

        navbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setQuestionsPallete();
                drawer.openDrawer(GravityCompat.END);
            }
        });
        rv = findViewById(R.id.rv);

        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        rv.setLayoutManager(manager);

        init();

    }

    public void setQuestionsPallete() {
        adapter = new QuestionNumberAdapter(mContext, listQuestions);
        rv.setAdapter(adapter);
        int cans = 0, cnotans = 0, cnotvis = 0, cmarkd = 0;
        for (int i = 0; i < listQuestions.size(); i++) {
            final QuestionsData data = listQuestions.get(i);
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

    private void init() {
        mContext = QuestionActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        testid = getIntent().getStringExtra("testid");
        marks = getIntent().getStringExtra("marks");
        time = getIntent().getStringExtra("time");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
//        tvTestTitle.setText(title);
//        tvTestTitle.setText("Hello this is long header name testing for home screen");
        tvTestTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvTestTitle.setSingleLine(true);
        tvTestTitle.setMarqueeRepeatLimit(50);
        tvTestTitle.setSelected(true);
        fetchQuestions();
    }

    private void fetchQuestions() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", testid);

        apiInterface.fetchQuestions(params).enqueue(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        listQuestions = response.body().getQuestionList();

                        adapter = new QuestionNumberAdapter(mContext, listQuestions);
                        rv.setAdapter(adapter);

                        showQuestion(0);
                        startTimer();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTimeUp){
            Intent in = new Intent(mContext, ResultsActivity.class);
            in.putExtra("testid", testid);
            in.putExtra("title", title);
            in.putExtra("type", type);
            startActivity(in);
        }
    }

    public void showQuestion(final int pos) {
        try {
            drawer.closeDrawer(GravityCompat.END);
        } catch (Exception e) {
        }
        selPos = pos;
        if (selPos == 0) {
            llPrev.setVisibility(GONE);
        } else {
            llPrev.setVisibility(VISIBLE);
        }
        if (selPos == (listQuestions.size() - 1)) {
            llNext.setVisibility(GONE);
        } else {
            llNext.setVisibility(VISIBLE);
        }
        QuestionsData data = listQuestions.get(pos);

        tvQno.setText("Question " + (pos + 1));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            tvQtitle.setText(Html.fromHtml(data.getTitle().replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
//            URLImageParser p = new URLImageParser(tvQtitle, this);
//            Spanned htmlSpan = Html.fromHtml(data.getTitle().replaceAll("\n", "<br>"), p, null);
//            tvQtitle.setText(htmlSpan);
//        } else {
//            tvQtitle.setText(Html.fromHtml(data.getTitle().replaceAll("\n", "<br>")));
//        }

        webView.loadDataWithBaseURL(null, data.getTitle(), "text/html", "UTF-8", null);

        tvMaxMarks.setText("Max Marks: " + data.getMarks());

        ArrayList<String> listAnswer = data.getOption_data();
        ArrayList<String> ans_images = data.getAns_images();

        try {
            rgQuestions.removeAllViews();
        } catch (Exception e) {
        }

        if (data.getQuestion_Image() != null && data.getQuestion_Image().trim().length() > 0) {
            Glide.with(mContext).load(data.getQuestion_Image()).into(imgQuestion);
            imgQuestion.setVisibility(VISIBLE);
        } else {
            imgQuestion.setVisibility(GONE);
        }

        for (int i = 0; i < listAnswer.size(); i++) {

            final LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.row_que_answers, null);
            final RadioButton rbAns = view.findViewById(R.id.rbAns);
            final AppCompatTextView tvAnswer = view.findViewById(R.id.tvAnswer);
            final ImageView imgAnswer = view.findViewById(R.id.imgAnswer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvAnswer.setText(Html.fromHtml(listAnswer.get(i).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvAnswer.setText(Html.fromHtml(listAnswer.get(i).replaceAll("\n", "<br>")));
            }
            if (listQuestions.get(selPos).getIsanswered().equals("1")) {
                if (Integer.parseInt(listQuestions.get(selPos).getUseranswerid()) == (i + 1)) {
                    rbAns.setChecked(true);
                }
            }
            if (ans_images.get(i) != null && ans_images.get(i).trim().length() > 0) {
                Glide.with(mContext).load(ans_images.get(i)).into(imgAnswer);
                imgAnswer.setVisibility(VISIBLE);
            } else {
                imgAnswer.setVisibility(GONE);
            }
            final int posp = i + 1;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    listQuestions.get(selPos).setIsanswered("1");
                    listQuestions.get(selPos).setUseranswerid("" + posp);
//                    llClearResponse.setVisibility(VISIBLE);
                    showQuestion(pos);
                }
            });
//            rbAns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b) {
//                        listQuestions.get(selPos).setIsanswered("1");
//                        listQuestions.get(selPos).setUseranswerid("" + posp);
////                        llClearResponse.setVisibility(VISIBLE);
////                        showQuestion(pos);
//                    }
//                }
//            });
            rgQuestions.addView(view);
        }

        if (listQuestions.get(selPos).getIsanswered().equals("1")) {
            llClearResponse.setVisibility(VISIBLE);
        } else {
            llClearResponse.setVisibility(GONE);
        }

        listQuestions.get(pos).setIsvisited("1");
        if (listQuestions.get(selPos).getMarkedforreview().equals("1")) {
            imgMarkedForReview.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            imgMarkedForReview.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @OnClick(R.id.imgMarkedForReview)
    public void onMarkedForReview() {
        if (listQuestions.get(selPos).getMarkedforreview().equals("1")) {
            listQuestions.get(selPos).setMarkedforreview("");
            imgMarkedForReview.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            listQuestions.get(selPos).setMarkedforreview("1");
            imgMarkedForReview.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @OnClick(R.id.llClearResponse)
    public void onClearResponse() {
        listQuestions.get(selPos).setIsanswered("");
        showQuestion(selPos);
    }

    @OnClick(R.id.llPrev)
    public void onPrev() {
        showQuestion(selPos - 1);
    }

    @OnClick(R.id.llNext)
    public void onNext() {
        showQuestion(selPos + 1);
    }

    @OnClick(R.id.llFinish)
    public void onFinish() {
        CustomDialog customDialog = new CustomDialog(QuestionActivity.this, listQuestions);
        customDialog.show();
    }

    public void onFinishApiCall() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", testid);
        long millis = System.currentTimeMillis() - startTime;
        String time = "" + TimeUnit.MILLISECONDS.toMinutes(millis);
        if (time.equals("0")) {
            time = "1";
        }
        params.put("timetaken", time);

        JSONArray ja = new JSONArray();

        for (int i = 0; i < listQuestions.size(); i++) {
            QuestionsData data = listQuestions.get(i);
            try {
                JSONObject jo = new JSONObject();
                jo.put("question_id", Integer.parseInt(data.getQuestion_id()));
                if (data.getUseranswerid().trim().length() == 0) {
                    jo.put("answer_id", 0);
                } else {
                    jo.put("answer_id", Integer.parseInt(data.getUseranswerid()));
                }
                if (data.getUseranswerid().equals("1")) {
                    jo.put("isvisited", 1);
                } else {
                    jo.put("isvisited", 0);
                }
                if (data.getIsanswered().equals("1")) {
                    jo.put("isanswered", 1);
                } else {
                    jo.put("isanswered", 0);
                }
                if (data.getMarkedforreview().equals("1")) {
                    jo.put("markedforreview", 1);
                } else {
                    jo.put("markedforreview", 0);
                }
                ja.put(jo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        params.put("questionarray", ja.toString());
        apiInterface.submitTest(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }

                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
                        Gson gsn = new Gson();

                        Utils.showToast(mContext, callback.getMessage());
                        if (callback.getStatus().equals("success")) {
                            isTimeUp = true;
                            Intent in = new Intent(mContext, ResultsActivity.class);
                            in.putExtra("testid", testid);
                            in.putExtra("title", title);
                            in.putExtra("type", type);
                            startActivity(in);
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

    private void startTimer() {
        try {
            startTime = System.currentTimeMillis();
            timer = new CountDownTimer((Integer.parseInt(time) * 60 * 1000), 1000) { // adjust the milli seconds here

                public void onTick(long millis) {
                    String hms = String.format("%02d:%02d:%02d",
                            (TimeUnit.MILLISECONDS.toHours(millis) -
                                    TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))),
                            (TimeUnit.MILLISECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))), (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                    tvTime.setText(hms);
                }

                public void onFinish() {
                    tvTime.setText("Time Up");
                    onFinishApiCall();
                }
            }.start();
        } catch (Exception e) {
        }
    }

    private boolean isTimeUp = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.quit_exam))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.submit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Utils.isNetworkAvailableShowToast(mContext)) {
                            dialogInterface.cancel();
                            onFinish();
                        }
                    }
                }).show();
    }
}
