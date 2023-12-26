package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.model.PlanData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PlanData> planData;
    private int selPos = 0;

    public SubscriptionAdapter(Context context, ArrayList<PlanData> planData) {
        this.mContext = context;
        this.planData = planData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_subscription_plans, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int pp) {

        final int position = holder.getAdapterPosition();

        final PlanData data = planData.get(position);

        holder.tvTitle.setText(data.getValidity_value());
        holder.tvPrice.setText("₹ "+data.getPlan_amount());
        holder.tvPriceO.setText("₹ "+data.getTotal_amount());
        if (data.getPlan_amount_type().equals("discount")){
            holder.tvDiscount.setText(data.getPlan_discount()+"% Off");
        } else {
            holder.tvDiscount.setText("₹ "+data.getPlan_discount()+" Off");
        }

        if (selPos == position) {
            holder.rlMain.setBackgroundResource(R.drawable.bg_color_selected);
            holder.llPrice.setBackgroundResource(R.drawable.w_card);
            holder.tvPriceO.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            holder.tvDiscount.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            holder.tvPriceO.setBackgroundResource(R.drawable.line2back);
            holder.img.setBackgroundResource(R.drawable.combopack);
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.rlMain.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            holder.llPrice.setBackgroundResource(R.drawable.b_card);
            holder.tvPriceO.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tvDiscount.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tvPriceO.setBackgroundResource(R.drawable.line_white);
            holder.img.setBackgroundResource(R.drawable.subyc);
            holder.imgCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selPos = position;
                notifyDataSetChanged();
                ((SubscriptionActivity)mContext).changePrice(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return planData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvTitle, tvPrice, tvPriceO, tvDiscount,tvStatus;
        RelativeLayout rlMain;
        LinearLayout llPrice;
        ImageView img,imgCheck;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rlMain);
            img = itemView.findViewById(R.id.img);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            llPrice = itemView.findViewById(R.id.llPrice);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPriceO = itemView.findViewById(R.id.tvPriceO);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
        }

    }

}
