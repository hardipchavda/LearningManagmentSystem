package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import app.preplotus.activities.CategoryActivity;

import app.preplotus.R;

import java.util.ArrayList;

import app.preplotus.model.CategoryData;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    Context mContext;
    ArrayList<CategoryData> list;
    private int selId = -1;

    public CategoryAdapter(Context context, ArrayList<CategoryData> list,int ppp) {
        this.mContext = context;
        this.list = list;
        this.selId = ppp;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_data_model, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int pp) {
        final int position = holder.getAdapterPosition();
        final CategoryData data = list.get(position);

        holder.tvCatTitle.setText(data.getCatName());
        holder.tvCatDescription.setText(data.getCatDescription());

        if (selId != -1) {
            if (position == selId) {
                holder.llCard.setBackgroundResource(R.drawable.bg_theme_color_border);
                holder.imgCheck.setVisibility(View.VISIBLE);
            } else {
                holder.llCard.setBackground(null);
                holder.imgCheck.setVisibility(View.GONE);
            }
        }

        Glide.with(mContext).load(data.getCatImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selId = position;
                notifyDataSetChanged();
                ((CategoryActivity) mContext).enableProceed(data.getCatName(),data.getCatId(),data.getTopic_name());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        AppCompatTextView tvCatTitle, tvCatDescription;
        LinearLayout llCard;
        ImageView imgCheck;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            tvCatTitle = itemView.findViewById(R.id.tvCatTitle);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            tvCatDescription = itemView.findViewById(R.id.tvCatDescription);
            llCard = itemView.findViewById(R.id.llCard);

        }
    }
}
