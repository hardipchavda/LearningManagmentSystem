package app.preplotus.activities;
        import static app.preplotus.utilities.Constants.COIN_VAL;
        import static app.preplotus.utilities.Constants.SUBSCRIBED;
        import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;
        import static app.preplotus.utilities.Constants.USER_ID;

        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.util.HashMap;
        import java.util.Map;


        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.util.Log;
        import android.view.View;
        import android.webkit.JavascriptInterface;
        import android.webkit.WebResourceError;
        import android.webkit.WebResourceRequest;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;

        import org.json.JSONObject;

        import app.preplotus.R;
        import app.preplotus.network.APIClient;
        import app.preplotus.network.APIInterface;
        import app.preplotus.utilities.Utils;
        import butterknife.ButterKnife;
        import butterknife.OnClick;
        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


public class CCAvenueActivity extends AppCompatActivity {
   // Intent mainIntent;
 //  private Context mContext;
   private String webUrl="";
   private byte[] postData;
   private Context mContext;
   private APIInterface apiInterface;
    private ProgressDialog pd;
    String encVal;
    String vResponse;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccavenue);
        ButterKnife.bind(this);
        init();

//get rsa key method
      //  get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }
    private void init() {
        mContext = CCAvenueActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

       render_page();


    }

    @OnClick(R.id.iconSubscriptionBack)
    public void onTermsClick(){
        Intent intent = new Intent(mContext, SubscriptionActivity.class);
        startActivity(intent);
    }
    public void render_page(){
        final WebView webView = (WebView) findViewById(R.id.ccavenue_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String _webUrl=getIntent().getStringExtra("webUrl");
        byte[] _postData=getIntent().getByteArrayExtra("postData");
        webView.postUrl(_webUrl,_postData);
        WebViewClient wc=new WebViewClient();

        webView.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest webreq, WebResourceError weberror) {
                super.onReceivedError(view, webreq, weberror);
                if (pd.isShowing()) {
                    pd.cancel();
                }

                Log.i("Listener", "Error on page: "+view.getUrl());


            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!pd.isShowing()) {
                    pd.show();
                }
                Log.i("Listener", "Started: "+url);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                Log.i("Listener", "Finished: "+url);

                if (pd.isShowing()) {
                    pd.cancel();
                }
                if(url.contains("androidwidget.php")){
                    try {

                        Uri uri = Uri.parse(url);
                        String order_details = uri.getQueryParameter("order_details");
                        Log.i("",url);
                        JSONObject jo =  new JSONObject(order_details);


                        if(jo.optString("order_status").equals("success")){

                            submitPaymentResponse("success",order_details);
                        }else{
                            submitPaymentResponse("failed",order_details);
                        }

                       // finish();

                        // Utils.showToast(mContext, order_status);

                    }catch(Exception ex){
                        Log.e("ccavenueError",ex.getMessage());
                    }



                }
                // Log.i("Listener", "Finish");

            }

        });
    }

    public void submitPaymentResponse(String status, String response) {

//        {"razorpay_payment_id":"pay_IbaY6fF9zBxh4q","razorpay_order_id":"order_IbaVtuDcF8xgGm","razorpay_signature":"335933f5eebb60b3a917f5310ed6b9d39242328257b07be1e950b96b562d6775","org_logo":"","org_name":"Razorpay Software Private Ltd","checkout_logo":"https:\/\/cdn.razorpay.com\/logo.png","custom_branding":false}
        String orderId=getIntent().getStringExtra("orderId");
        String couponCode=getIntent().getStringExtra("couponCode");
        int coinUsed=getIntent().getIntExtra("coinUsed",0);
        if (!pd.isShowing()) {
            pd.show();
        }
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userid", Utils.getPrefData(USER_ID, mContext));
            params.put("supergroup_id", Utils.getPrefData(SUPER_GROUP_ID, mContext));

            if (status.equals("success")) {
                JSONObject jo = new JSONObject(response);

                params.put("razorpay_payment_id", jo.optString("razorpay_payment_id"));
                params.put("razorpay_order_id", jo.optString("razorpay_order_id"));
                params.put("razorpay_signature", jo.optString("razorpay_signature"));
                params.put("is_success", "1");
                if (couponCode.trim().length() > 0) {
                    params.put("coupon_code", couponCode);
                }
            } else {
                params.put("razorpay_payment_id", "");
                params.put("razorpay_order_id", orderId);
                params.put("razorpay_signature", "");
                params.put("is_success", "0");
            }

            apiInterface.apiUpdatePayment(params).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (pd.isShowing()) {
                        pd.cancel();
                    }

                    try {

                        if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                            JSONObject jo = new JSONObject(response.body().string());
                            if (jo.optString("status").equals("success") && status.equals("success")) {
//                                {"status":"success","message":"User Subscribed Successfully"}
                                if (coinUsed > 0) {
                                    redeemCoins(coinUsed);
                                }
                                Utils.setPrefData(SUBSCRIBED, "yes", mContext);
                                new androidx.appcompat.app.AlertDialog.Builder(mContext).setTitle("Congratulations!").setMessage(jo.optString("message")).setCancelable(false).setPositiveButton("Go to Home", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                            } else {
                                new AlertDialog.Builder(mContext).setTitle("Payment Failed!").setMessage(jo.optString("message")).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                        Intent intent = new Intent(mContext, SubscriptionActivity.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                       Intent intent = new Intent(mContext, ContactUsActivity.class);
                                        startActivity(intent);

                                    }
                                }).show();
                            }


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

        } catch (Exception e) {
        }

    }

    private void redeemCoins(final int coinUsed) {

        Map<String, String> params = new HashMap<>();
        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("coins", "" + coinUsed);

        apiInterface.apiRedeemCoins(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }




}
