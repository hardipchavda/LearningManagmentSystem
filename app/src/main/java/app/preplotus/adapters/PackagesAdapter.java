package app.preplotus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import app.preplotus.R;

import java.util.ArrayList;

import app.preplotus.activities.TestsActivity;
import app.preplotus.model.PackageData;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.viewHolder> {

    ArrayList<PackageData> list;
    Context mContext;

    public PackagesAdapter(Context context, ArrayList<PackageData> list) {
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_packages, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final PackageData data = list.get(position);

        Glide.with(mContext).load(data.getImage()).into(holder.imageView);

        holder.textView_package.setText(data.getTitle());
//        holder.txt_explore.setText(homeSecondDataModels.get(position).getExplore());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, TestsActivity.class);
                intent.putExtra("id",data.getId());
                intent.putExtra("title",data.getTitle());
                intent.putExtra("type","package");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView_package, txt_explore;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView_package = itemView.findViewById(R.id.txt_package);
            txt_explore = itemView.findViewById(R.id.txt_explore);
        }
    }
}
