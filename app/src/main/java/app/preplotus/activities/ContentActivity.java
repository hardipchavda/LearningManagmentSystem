package app.preplotus.activities;

import static app.preplotus.utilities.Constants.ABOUT_US;
import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_GUIDE;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import app.preplotus.R;
import app.preplotus.databinding.ActivityContentBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
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

//    @BindView(R.id.btnPlay)
//    FloatingActionButton btnPlay;
    ImageButton exo_play;
    AppCompatSpinner spinSpeed;

    ImageButton exo_pause;

    private Context mContext;
    private String type,play_url,file_path,file_url;

    private APIInterface apiInterface;
    private ProgressDialog pd;

    private ExoPlayer player;
    private LinearLayout llProgress;

    private ActivityContentBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        ButterKnife.bind(this);
        llProgress = findViewById(R.id.llProgress);
        exo_pause = findViewById(R.id.exo_pause);
        exo_play = findViewById(R.id.exoPlay);
        spinSpeed = findViewById(R.id.spinSpeed);
        init();
    }

    private void init() {
        mContext = ContentActivity.this;
        type = getIntent().getStringExtra("type");
        file_path = getIntent().getStringExtra("file_path");
        file_url = getIntent().getStringExtra("file_url");

        if (file_url!=null && file_url.length()>0){
            play_url = file_url;
        } else if (file_path!=null && file_path.length()>0){
            play_url = file_path;
        }

        if (play_url!=null){
//            btnPlay.setVisibility(View.VISIBLE);
            Log.e("ttt","yes0");
            binding.rlMusic.setVisibility(View.VISIBLE);
            Log.e("ttt","yes1");
            exo_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.setPlayWhenReady(true);
                    llProgress.setVisibility(View.VISIBLE);
                    exo_play.setVisibility(View.GONE);
                    exo_pause.setVisibility(View.VISIBLE);
                }
            });
            exo_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.setPlayWhenReady(false);
                    llProgress.setVisibility(View.GONE);
                    exo_play.setVisibility(View.VISIBLE);
                    exo_pause.setVisibility(View.GONE);
                }
            });
            initAudio();
//            btnPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnPlay.setVisibility(View.GONE);
//                    rlMusic.setVisibility(View.VISIBLE);
//                    initAudio();
//                }
//            });
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        if (type.equals("aboutus")) {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(getResources().getString(R.string.about_us));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvContent.setText(Html.fromHtml(Utils.getPrefData(ABOUT_US, mContext).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.tvContent.setText(Html.fromHtml(Utils.getPrefData(ABOUT_US, mContext).replaceAll("\n", "<br>")));
            }
        } else if (type.equals("userguide")) {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(getResources().getString(R.string.user_guide));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvContent.setText(Html.fromHtml(Utils.getPrefData(USER_GUIDE, mContext).replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.tvContent.setText(Html.fromHtml(Utils.getPrefData(USER_GUIDE, mContext).replaceAll("\n", "<br>")));
            }
        } else if (type.equals("details")) {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(getIntent().getStringExtra("title"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content").replaceAll("\n", "<br>"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content").replaceAll("\n", "<br>")));
            }
        } else if (type.equals("url")) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(getIntent().getStringExtra("title"));
//            webView.setWebViewClient(new WebViewClient());
//            webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            binding.webView.getSettings().setBuiltInZoomControls(false);
            binding.webView.getSettings().setLoadWithOverviewMode(false);
            binding.webView.getSettings().setLoadsImagesAutomatically(true);

            binding.webView.getSettings().setUseWideViewPort(false);
            binding.webView.setWebViewClient(new WebViewClient());
            binding.webView.loadUrl(getIntent().getStringExtra("content"));
        } else {
            binding.webView.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(getIntent().getStringExtra("title"));
//            webView.setWebViewClient(new WebViewClient());
//            webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            binding.webView.getSettings().setBuiltInZoomControls(false);
            binding.webView.getSettings().setLoadWithOverviewMode(false);
            binding.webView.getSettings().setLoadsImagesAutomatically(true);

            binding.webView.getSettings().setUseWideViewPort(false);
            binding.webView.setWebChromeClient(new WebChromeClient());
            fetchContent();
        }
        disableCopyPaste();
        binding.tvContent.setMovementMethod(new ScrollingMovementMethod());
        binding.tvTitle.setSelected(true);

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (player!=null){
                player.setPlayWhenReady(false);
            }
        } catch (Exception e){}
    }

    private void initAudio () {

        DataSource.Factory factory = new DefaultDataSource.Factory(mContext);

        ProgressiveMediaSource source = new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(play_url));

        DefaultMediaSourceFactory ms = new DefaultMediaSourceFactory(factory);

        player = new ExoPlayer.Builder(mContext).setMediaSourceFactory(ms).build();

        player.setMediaSource(source);

        player.prepare();

//        player.setPlayWhenReady(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.controls.setPlayer(player);

                spinSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));

                        if (i==0) {
                            PlaybackParameters param = new PlaybackParameters(1f);
                            player.setPlaybackParameters(param);
                        } else if (i==1) {
                            PlaybackParameters param = new PlaybackParameters(1.25f);
                            player.setPlaybackParameters(param);
                        }  else if (i==2) {
                            PlaybackParameters param = new PlaybackParameters(1.50f);
                            player.setPlaybackParameters(param);
                        }  else if (i==3) {
                            PlaybackParameters param = new PlaybackParameters(1.75f);
                            player.setPlaybackParameters(param);
                        } else if (i==4) {
                            PlaybackParameters param = new PlaybackParameters(2f);
                            player.setPlaybackParameters(param);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        },100);
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

                        binding.webView.loadDataWithBaseURL(null, content, null, "UTF-8", null);
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
        binding.webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        binding.webView.setLongClickable(false);
        binding.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        binding.tvContent.setLongClickable(false);
        binding.tvContent.setHapticFeedbackEnabled(false);
        binding.webView.setHapticFeedbackEnabled(false);
    }

}
