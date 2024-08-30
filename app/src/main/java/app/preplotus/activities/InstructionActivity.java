package app.preplotus.activities;

import static app.preplotus.utilities.Constants.USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;

import app.preplotus.R;
import app.preplotus.databinding.ActivityInstructionsBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructionActivity extends AppCompatActivity {


    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    private String testid,title,marks,time,type;


    private ActivityInstructionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructionsBinding.inflate(getLayoutInflater());
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        mContext = InstructionActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        testid = getIntent().getStringExtra("testid");
        marks = getIntent().getStringExtra("marks");
        time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        fetchInstructions();

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartTest();
            }
        });

    }

    private void fetchInstructions() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("testid", testid);
//        params.put("userid", "1");
//        params.put("testid", "3");

        apiInterface.fetchTestInstructions(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (pd.isShowing()) {
                    pd.cancel();
                }

                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        JSONObject jo = new JSONObject(response.body().string());
                        String content = jo.optString("Exam Instruction");
                        String allowed = jo.optString("IsallowdToattempt");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.tvInstructions.setText(Html.fromHtml(content.replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            binding.tvInstructions.setText(Html.fromHtml(content.replaceAll("\n", "<br>")));
                        }

//                        if (allowed.equals("False")) {
//                            tvCompleted.setVisibility(View.VISIBLE);
//                            btnStart.setVisibility(View.GONE);
//                        }

                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.cancel();
                }

            }
        });
    }


    public void onStartTest(){
        if (binding.checkReady.isChecked()){
            Intent in = new Intent(mContext,QuestionActivity.class);
            in.putExtra("testid",testid);
            in.putExtra("title",title);
            in.putExtra("marks",marks);
            in.putExtra("time",time);
            in.putExtra("type",type);
            startActivity(in);
        }
    }

}