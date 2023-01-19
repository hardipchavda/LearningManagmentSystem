package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.model.CouponsData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<CouponsData> couponsData;
    private int selPos = 0;

    public CouponsAdapter(Context context, ArrayList<CouponsData> couponsData) {
        this.mContext = context;
        this.couponsData = couponsData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_coupons, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int pp) {

        final int position = holder.getAdapterPosition();

        final CouponsData data = couponsData.get(position);

        holder.tvCouponCode.setText(data.getCoupon_code());
        String type = "";
        if (data.getCoupon_amount_or_discount().equals("discount")){
            type = " %";
        }
        String str = "Get "+ data.getCoupon_code_value()+type +" Off";
        holder.tvDiscount.setText(str);

        holder.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SubscriptionActivity)mContext).checkCoupon(data.getCoupon_code());
            }
        });

    }

    @Override
    public int getItemCount() {
        return couponsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvCouponCode, tvApply, tvDiscount;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvCouponCode = itemView.findViewById(R.id.tvCouponCode);
            tvApply = itemView.findViewById(R.id.tvApply);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
        }

    }

}

