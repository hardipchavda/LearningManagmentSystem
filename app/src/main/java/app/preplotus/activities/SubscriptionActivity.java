package app.preplotus.activities;

import static app.preplotus.utilities.Constants.SUBSCRIBED;
import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;
import static app.preplotus.utilities.Constants.USER_EMAIL;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_NAME;
import static app.preplotus.utilities.Constants.USER_PHONE;
import static app.preplotus.utilities.Constants.CCAVENUE_API_URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import app.preplotus.R;
import app.preplotus.adapters.CouponsAdapter;
import app.preplotus.adapters.SubscriptionAdapter;
import app.preplotus.model.CouponsData;
import app.preplotus.model.PlanData;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.PaymentDialog;
import app.preplotus.utilities.Utils;
import com.razorpay.Checkout;

import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubscriptionActivity extends AppCompatActivity implements PaymentResultWithDataListener {


    public int planPrice = 0;
    public int finalPrice = 0;
    @BindView(R.id.rvPlans)
    RecyclerView rvPlans;
    @BindView(R.id.rvCoupons)
    RecyclerView rvCoupons;
    @BindView(R.id.llGroups)
    LinearLayout llGroups;
    @BindView(R.id.llApplidCoupon)
    LinearLayout llApplidCoupon;
    @BindView(R.id.tvAppliedCode)
    AppCompatTextView tvAppliedCode;
    @BindView(R.id.tvFinalPrice)
    AppCompatTextView tvFinalPrice;
    @BindView(R.id.etCouponCode)
    AppCompatEditText etCouponCode;
    @BindView(R.id.llApplyCoupon)
    LinearLayout llApplyCoupon;
    private String planName = "", couponCode = "", couponValue = "", userCoins = "", planId = "", validityId = "";
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String orderId = "";
    private int coinUsed = 0;

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        init();
    }

    @Override
    public void onBackPressed() {
        if (from!=null && from.equals("notification")){
            Intent in = new Intent(mContext,MainActivity.class);
            startActivity(in);
            finish();
        } else {
            super.onBackPressed();

        }
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.tvContactUs)
    public void onContactUs() {
        Intent in = new Intent(mContext, ContactUsActivity.class);
        startActivity(in);
    }

    private void init() {
        mContext = SubscriptionActivity.this;

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        rvPlans.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvPlans.setNestedScrollingEnabled(false);
        rvCoupons.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvCoupons.setNestedScrollingEnabled(false);
        Checkout.preload(getApplicationContext());
        fetchPlans();

    }

    private void fetchPlans() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("supergroup_id", Utils.getPrefData(SUPER_GROUP_ID, mContext));

        apiInterface.fetchSubscriptionDetails(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        JSONObject jo = new JSONObject(response.body().string());

                        JSONArray packs = jo.getJSONArray("plan_info");

                        JSONObject jOne = packs.getJSONObject(0);
                        planId = jOne.optString("plan_id");
                        JSONArray validity = jOne.getJSONArray("Validity_info");
                        JSONArray coupons = jo.getJSONArray("coupon_code_info");

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<PlanData>>() {
                        }.getType();
                        ArrayList<PlanData> list = gson.fromJson(validity.toString(), listType);
                        changePrice(list.get(0));
                        SubscriptionAdapter adapter = new SubscriptionAdapter(mContext, list);
                        rvPlans.setAdapter(adapter);

                        Gson gsontwo = new Gson();
                        Type cType = new TypeToken<ArrayList<CouponsData>>() {
                        }.getType();
                        ArrayList<CouponsData> listC = gsontwo.fromJson(coupons.toString(), cType);

                        CouponsAdapter adapterC = new CouponsAdapter(mContext, listC);
                        rvCoupons.setAdapter(adapterC);


                        try {

                            JSONArray jGropus = jo.getJSONArray("examGropus");

                            for (int i = 0; i < jGropus.length(); i++) {
                                String str = jGropus.getString(i);
                                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.row_benifit_includes, null);
                                AppCompatTextView tvNameGroup = ll.findViewById(R.id.tvNameGroup);
                                tvNameGroup.setText(str);
                                llGroups.addView(ll);
                            }

                        } catch (Exception e) {
                        }

                        userCoins = "" + jo.optInt("userCoins");

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

    public void appyCoupon(final CouponsData data) {

        if (planPrice >= Integer.parseInt(data.getMinimum_order_value())) {
            if (data.getCoupon_amount_or_discount().equals("discount")) {
                int pp = (Integer.parseInt(data.getCoupon_code_value()) * planPrice) / 100;
                finalPrice = planPrice - pp;
            } else {
                finalPrice = planPrice - Integer.parseInt(data.getCoupon_code_value());
            }
            llApplidCoupon.setVisibility(View.VISIBLE);
            llApplyCoupon.setVisibility(View.GONE);
            tvAppliedCode.setText("Applied Coupon - " + data.getCoupon_code());
            tvFinalPrice.setText("₹ " + finalPrice);
            couponCode = data.getCoupon_code();
            couponValue = "" + (planPrice - finalPrice);
        } else {
            Utils.showToast(mContext, "Minimum Order Value is " + data.getMinimum_order_value() + " for this Coupon.");
        }

    }

    @OnClick(R.id.tvRemoveCoupons)
    public void onRemoveCoupon() {
        llApplidCoupon.setVisibility(View.GONE);
        llApplyCoupon.setVisibility(View.VISIBLE);
        tvFinalPrice.setText("₹ " + planPrice);
        finalPrice = planPrice;
        etCouponCode.setText("");
        couponCode = "";
        couponValue = "";
    }

    @OnClick(R.id.tvApplyCoupon)
    public void onApplyCoupon() {
        if (!Utils.isNullE(etCouponCode)) {
            checkCoupon(Utils.valE(etCouponCode));
        }
    }

    public void changePrice(final PlanData data) {
        planPrice = Integer.parseInt(data.getPlan_amount());
        planName = data.getValidity_value();
        validityId = data.getValidity_id();
        couponCode = "";
        couponValue = "";
        llApplidCoupon.setVisibility(View.GONE);
        llApplyCoupon.setVisibility(View.VISIBLE);
        tvFinalPrice.setText("₹ " + planPrice);
        finalPrice = planPrice;
        etCouponCode.setText("");
    }

    public void checkCoupon(final String coupon) {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("supergroup_id", Utils.getPrefData(SUPER_GROUP_ID, mContext));
        params.put("coupon_code", coupon);

        apiInterface.applyCoupon(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        JSONObject jo = new JSONObject(response.body().string());

                        if (jo.optString("status").equals("success")) {
                            JSONObject info = jo.getJSONObject("coupon_code_info");
                            CouponsData data = new CouponsData();
                            data.setCoupon_code(info.optString("coupon_code"));
                            data.setCoupon_amount_or_discount(info.optString("coupon_amount_type"));
                            data.setCoupon_code_value(info.optString("coupon_amount_or_discount"));
                            data.setMinimum_order_value(info.optString("coupon_min_order"));
                            appyCoupon(data);
                        } else {
                            Utils.showToast(mContext, jo.optString("message"));
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

    }

    @OnClick(R.id.cvProceed)
    public void onProceed() {
        if (!Utils.getPrefData(SUBSCRIBED,mContext).equals("yes")) {
            PaymentDialog paymentDialog = new PaymentDialog(mContext, planName, "" + planPrice, couponCode, couponValue, "" + finalPrice, userCoins, planId, validityId);
            paymentDialog.show(getSupportFragmentManager(), "payment");
      } else {
            new AlertDialog.Builder(mContext).setTitle("You are already Subscribed to this SuperGroup!").setPositiveButton("Okay", null).show();
        }
    }

    public String generate_order_number(){
        char[] chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();

        Random random1 = new Random();
        for (int i = 0; i < 10; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        String random_string = sb1.toString();
        return random_string;
    }

    public void onPayCCAvenue(String orderId){


        String postData="userid="+Utils.getPrefData(USER_ID, mContext)+"&"+
                "userfullname="+ Utils.getPrefData(USER_NAME, mContext)+"&"+
                "useremail="+Utils.getPrefData(USER_EMAIL, mContext)+"&"+
                "userphone="+Utils.getPrefData(USER_PHONE, mContext)+"&"+
                "supergroup_id="+Utils.getPrefData(SUPER_GROUP_ID, mContext)+"&"+
                "plan_id="+planId+"&"+
                "validity_id="+validityId+"&"+
                "order_id="+orderId+"&"+
                "total_amount="+finalPrice;


        String webUrl=CCAVENUE_API_URL;
        Intent ccav_in = new Intent(mContext, CCAvenueActivity.class);
        ccav_in.putExtra("webUrl",webUrl);
        ccav_in.putExtra("postData",postData.getBytes());
        ccav_in.putExtra("orderId",orderId);
        ccav_in.putExtra("couponCode",couponCode);
        ccav_in.putExtra("coinUsed",coinUsed);

        finish();
        startActivity(ccav_in);

     //   setContentView(webView);
     //   webView.postUrl(webUrl,postData.getBytes());
    }
    public void onMakePayment(final String finalPrice, final int coinUsed) {

        if (!pd.isShowing()) {
            pd.show();
        }




        this.coinUsed = coinUsed;
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("supergroup_id", Utils.getPrefData(SUPER_GROUP_ID, mContext));
        params.put("plan_id", planId);
        params.put("validity_id", validityId);
        params.put("total_amount", "" + finalPrice);

        apiInterface.apiCreateOrder(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    if (pd.isShowing()) {
                        pd.cancel();
                    }


                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        JSONObject jo = new JSONObject(response.body().string());

                        if (jo.optString("status").equals("success")) {
                            String order_id = jo.optString("order_id");
                            //String rzp_key = jo.optString("razorpay_key");
                           // onPay(order_id, rzp_key);
                            onPayCCAvenue(order_id);
                        } else {
                            Utils.showToast(mContext, jo.optString("message"));
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




/* Previous codes for Razor Pay
        if (!pd.isShowing()) {
            pd.show();
        }
        this.coinUsed = coinUsed;
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("supergroup_id", Utils.getPrefData(SUPER_GROUP_ID, mContext));
        params.put("plan_id", planId);
        params.put("validity_id", validityId);
        params.put("total_amount", "" + finalPrice);

        apiInterface.apiCreateOrder(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {

                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {

                        JSONObject jo = new JSONObject(response.body().string());

                        if (jo.optString("status").equals("success")) {
                            String order_id = jo.optString("order_id");
                            String rzp_key = jo.optString("razorpay_key");
                            onPay(order_id, rzp_key);
                        } else {
                            Utils.showToast(mContext, jo.optString("message"));
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
*/
    }

    private void onPay(final String orderId, final String rzp_key) {
        this.orderId = orderId;
        final Checkout co = new Checkout();
        co.setKeyID(rzp_key);
        try {
            JSONObject options = new JSONObject();
            options.put("name", mContext.getResources().getString(R.string.app_name));
            options.put("description", planName + " Plan");
            options.put("order_id", orderId);
            options.put("currency", "INR");
//            options.put("theme.color", "00A0E3");
            options.put("amount", "" + finalPrice);
            options.put("prefill.email", Utils.getPrefData(USER_EMAIL, mContext));
            options.put("prefill.contact", "");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", false);
            retryObj.put("max_count", 1);
            options.put("retry", retryObj);
            co.open(SubscriptionActivity.this, options);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        submitPaymentResponse("success", paymentData.getData().toString());
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

        submitPaymentResponse("failed", "");
    }

    public void submitPaymentResponse(String status, String response) {

//        {"razorpay_payment_id":"pay_IbaY6fF9zBxh4q","razorpay_order_id":"order_IbaVtuDcF8xgGm","razorpay_signature":"335933f5eebb60b3a917f5310ed6b9d39242328257b07be1e950b96b562d6775","org_logo":"","org_name":"Razorpay Software Private Ltd","checkout_logo":"https:\/\/cdn.razorpay.com\/logo.png","custom_branding":false}

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
                                new AlertDialog.Builder(mContext).setTitle("Congratulations!").setMessage(jo.optString("message")).setCancelable(false).setPositiveButton("Go to Home", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                    }
                                }).setNegativeButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(mContext, ContactUsActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                            }

                            /* Previous codes for Razor Pay
                            if (jo.optString("status").equals("success") && status.equals("success")) {
//                                {"status":"success","message":"User Subscribed Successfully"}
                                if (coinUsed > 0) {
                                    redeemCoins(coinUsed);
                                }
                                Utils.setPrefData(SUBSCRIBED, "yes", mContext);
                                new AlertDialog.Builder(mContext).setTitle("Congratulations!").setMessage(jo.optString("message")).setCancelable(false).setPositiveButton("Go to Home", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
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
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                        Intent intent = new Intent(mContext, ContactUsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                    }
                                }).show();
                            }*/
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