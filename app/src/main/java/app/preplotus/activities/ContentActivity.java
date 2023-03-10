package app.preplotus.activities;

import static app.preplotus.utilities.Constants.ABOUT_US;
import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_GUIDE;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import app.preplotus.R;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;
    @BindView(R.id.tvContent)
    AppCompatTextView tvContent;
    @BindView(R.id.webView)
    WebView webView;
    private Context mContext;
    private String type;

    private APIInterface apiInterface;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = ContentActivity.this;
        type = getIntent().getStringExtra("type");

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        if (type.equals("aboutus")) {
            tvContent.setVisibility(View.VISIBLE);
            tvTitle.setText(getResources().getString(R.string.about_us));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(Utils.getPrefData(ABOUT_US, mContext).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(Utils.getPrefData(ABOUT_US, mContext).replaceAll("\n", "<br>")));
            }
        } else if (type.equals("userguide")) {
            tvContent.setVisibility(View.VISIBLE);
            tvTitle.setText(getResources().getString(R.string.user_guide));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(Utils.getPrefData(USER_GUIDE, mContext).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(Utils.getPrefData(USER_GUIDE, mContext).replaceAll("\n", "<br>")));
            }
        } else if (type.equals("details")) {
            tvContent.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content").replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content").replaceAll("\n", "<br>")));
            }
        } else if (type.equals("url")) {
            webView.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
//            webView.setWebViewClient(new WebViewClient());
//            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setLoadWithOverviewMode(false);
            webView.getSettings().setLoadsImagesAutomatically(true);

            webView.getSettings().setUseWideViewPort(false);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(getIntent().getStringExtra("content"));
        } else {
            webView.setVisibility(View.VISIBLE);
            tvTitle.setText(getIntent().getStringExtra("title"));
//            webView.setWebViewClient(new WebViewClient());
//            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setLoadWithOverviewMode(false);
            webView.getSettings().setLoadsImagesAutomatically(true);

            webView.getSettings().setUseWideViewPort(false);
            webView.setWebChromeClient(new WebChromeClient());
            fetchContent();
        }
        disableCopyPaste();
        tvContent.setMovementMethod(new ScrollingMovementMethod());
        tvTitle.setSelected(true);
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void fetchContent() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));
        params.put("subtopicid", getIntent().getStringExtra("subtopicid"));

        apiInterface.fetchContent(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (pd.isShowing()) {
                    pd.cancel();
                }

                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        
                        String content = response.body().string();

                        webView.loadDataWithBaseURL(null, content, null, "UTF-8", null);
                        disableCopyPaste();
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

    private void disableCopyPaste() {
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        tvContent.setLongClickable(false);
        tvContent.setHapticFeedbackEnabled(false);
        webView.setHapticFeedbackEnabled(false);
    }

}
