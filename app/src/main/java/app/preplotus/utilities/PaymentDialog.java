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
import app.preplotus.databinding.ActivityContentBinding;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.databinding.BtmshtPaymentBinding;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;




public class PaymentDialog extends BottomSheetDialogFragment {

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String planName, planPrice, couponCode, couponDiscount, finalPrice, userCoins, planId, validityId;
    private int coinUsed = 0;
    private boolean isRedeem = false;

    private BtmshtPaymentBinding binding;

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
        binding = BtmshtPaymentBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        View contentView = View.inflate(getContext(), R.layout.btmsht_payment, null);
//        ButterKnife.bind(this, contentView);
        init();
        dialog.setContentView(binding.getRoot());
    }

    private void init() {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
//        getPreferences();

        if (couponCode.trim().length() != 0) {
            binding.llCoupon.setVisibility(View.VISIBLE);
            binding.llCouponDiscount.setVisibility(View.VISIBLE);
            binding.tvCouponCode.setText(couponCode);
            binding.tvCouponDiscount.setText("₹ "+couponDiscount);
        }
        if (userCoins.trim().length() > 0 && Integer.parseInt(userCoins) > COIN_VAL) {
            binding.llReedemCoins.setVisibility(View.VISIBLE);
            binding.tvLabelCoins.setText("You have " + userCoins + " Coins");
        }
        binding.tvFinalAmount.setText("₹ "+finalPrice);
        binding.tvPlanName.setText(planName);
        binding.tvPlanPrice.setText("₹ "+planPrice);

        binding.tvReedemCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRedeemCoins();
            }
        });

        binding.tvMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMakePayment();
            }
        });

    }


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
            binding.tvFinalAmount.setText("₹ "+finalPrice);
            binding.tvLabelCoins.setText("Coins discount \n(Coins used " + (coinUsed * COIN_VAL) + ")");
            binding.tvReedemCoins.setText("" + coinUsed);
            binding.tvReedemCoins.setBackgroundColor(ContextCompat.getColor(mContext,R.color.trans));
            binding.tvReedemCoins.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }


    public void onMakePayment() {
          dismiss();

               ((SubscriptionActivity) mContext).onMakePayment(finalPrice,coinUsed * COIN_VAL);

    }


}

