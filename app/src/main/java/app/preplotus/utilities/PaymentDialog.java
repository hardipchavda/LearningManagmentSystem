package app.preplotus.utilities;

import static app.preplotus.utilities.Constants.COIN_VAL;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import app.preplotus.R;
import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PaymentDialog extends BottomSheetDialogFragment {

    @BindView(R.id.tvPlanName)
    AppCompatTextView tvPlanName;
    @BindView(R.id.tvCouponCode)
    AppCompatTextView tvCouponCode;
    @BindView(R.id.tvPlanPrice)
    AppCompatTextView tvPlanPrice;
    @BindView(R.id.tvCouponDiscount)
    AppCompatTextView tvCouponDiscount;
    @BindView(R.id.tvFinalAmount)
    AppCompatTextView tvFinalAmount;
    @BindView(R.id.tvReedemCoins)
    AppCompatTextView tvReedemCoins;
    @BindView(R.id.tvLabelCoins)
    AppCompatTextView tvLabelCoins;
    @BindView(R.id.llReedemCoins)
    LinearLayout llReedemCoins;
    @BindView(R.id.llCouponDiscount)
    LinearLayout llCouponDiscount;
    @BindView(R.id.llCoupon)
    LinearLayout llCoupon;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String planName, planPrice, couponCode, couponDiscount, finalPrice, userCoins, planId, validityId;
    private int coinUsed = 0;
    private boolean isRedeem = false;

    public PaymentDialog(Context contex, String planName, String planPrice, String couponCode, String couponDiscount, String finalPrice, String userCoins, String planId, String validityId) {
        mContext = contex;
        this.planName = planName;
        this.planPrice = planPrice;
        this.couponCode = couponCode;
        this.couponDiscount = couponDiscount;
        this.finalPrice = finalPrice;
        this.userCoins = userCoins;
        this.planId = planId;
        this.validityId = validityId;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.btmsht_payment, null);
        ButterKnife.bind(this, contentView);
        init();
        dialog.setContentView(contentView);
    }

    private void init() {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
//        getPreferences();

        if (couponCode.trim().length() != 0) {
            llCoupon.setVisibility(View.VISIBLE);
            llCouponDiscount.setVisibility(View.VISIBLE);
            tvCouponCode.setText(couponCode);
            tvCouponDiscount.setText("₹ "+couponDiscount);
        }
        if (userCoins.trim().length() > 0 && Integer.parseInt(userCoins) > COIN_VAL) {
            llReedemCoins.setVisibility(View.VISIBLE);
            tvLabelCoins.setText("You have " + userCoins + " Coins");
        }
        tvFinalAmount.setText("₹ "+finalPrice);
        tvPlanName.setText(planName);
        tvPlanPrice.setText("₹ "+planPrice);
    }

    @OnClick(R.id.tvReedemCoins)
    public void onRedeemCoins() {
        if (!isRedeem) {
            isRedeem = true;
            int maxUsedCoins = Integer.parseInt(finalPrice) / 2;
            int coins = Integer.parseInt(userCoins);
            int coinsVal = coins / COIN_VAL;

            if (coinsVal > maxUsedCoins) {
                finalPrice = "" + maxUsedCoins;
                coinUsed = maxUsedCoins;
            } else {
                finalPrice = "" + (Integer.parseInt(finalPrice) - coinsVal);
                coinUsed = coinsVal;
            }
            tvFinalAmount.setText("₹ "+finalPrice);
            tvLabelCoins.setText("Coins discount \n(Coins used " + (coinUsed * COIN_VAL) + ")");
            tvReedemCoins.setText("" + coinUsed);
            tvReedemCoins.setBackgroundColor(ContextCompat.getColor(mContext,R.color.trans));
            tvReedemCoins.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }

    @OnClick(R.id.tvMakePayment)
    public void onMakePayment() {
          dismiss();

               ((SubscriptionActivity) mContext).onMakePayment(finalPrice,coinUsed * COIN_VAL);

    }


}

