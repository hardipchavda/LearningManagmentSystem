package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.model.SubscriptionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MySubscriptionsAdapter extends RecyclerView.Adapter<MySubscriptionsAdapter.viewHolder> {

    Context mContext;
    ArrayList<SubscriptionData> list;

    public MySubscriptionsAdapter(Context context, ArrayList<SubscriptionData> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_subscriptions, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final SubscriptionData data = list.get(position);

        holder.tvTitle.setText(data.getValidity_name()+" plan for "+data.getSupergroup_name());
        try {
            String orgdate = data.getStart_date();
            String enddate = data.getEnd_date();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat outputFormat2 = new SimpleDateFormat("dd MMM yyyy");
            Date date = inputFormat.parse(orgdate);
            Date date2 = inputFormat2.parse(enddate);
            String str = outputFormat.format(date);
            String str2 = outputFormat2.format(date2);
            holder.tvDate.setText(str+" - "+str2);
        } catch (Exception e){}

        if (data.getIs_active().equals("1")){
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            holder.tvStatus.setText("Active");
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.tvStatus.setText("Expired");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvTitle,tvDate,tvStatus;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }
}
