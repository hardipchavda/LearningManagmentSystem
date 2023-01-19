package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.model.CoinsData;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyCoinsAdapter extends RecyclerView.Adapter<MyCoinsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<CoinsData> coinsData;

    public MyCoinsAdapter(Context context, ArrayList<CoinsData> coinsData) {
        this.mContext = context;
        this.coinsData = coinsData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_coin_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        final CoinsData data = coinsData.get(position);

        try {

            String orgdate = data.getDate();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM");
            Date date = inputFormat.parse(orgdate);
            String str = outputFormat.format(date);
            holder.tvDate.setText(str.split("-")[0]);
            holder.tvMonth.setText(str.split("-")[1]);

        } catch (Exception e){}

        holder.tvCoins.setText(data.getCoinAmount()+" Coins");
        holder.tvFrom.setText(data.getFrom());

    }

    @Override
    public int getItemCount() {
        return coinsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvDate, tvMonth, tvCoins, tvFrom;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvCoins = itemView.findViewById(R.id.tvCoins);
            tvFrom = itemView.findViewById(R.id.tvFrom);
        }
    }

}
