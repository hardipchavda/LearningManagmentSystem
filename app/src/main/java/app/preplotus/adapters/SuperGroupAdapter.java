package app.preplotus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import app.preplotus.R;
import app.preplotus.activities.CategoryActivity;
import app.preplotus.model.SuperGroupData;

import java.util.ArrayList;

public class SuperGroupAdapter extends RecyclerView.Adapter<SuperGroupAdapter.viewHolder> {
    Context mContext;
    ArrayList<SuperGroupData> list;

    public SuperGroupAdapter(Context context, ArrayList<SuperGroupData> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_super_group, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final SuperGroupData data = list.get(position);

        holder.tvTitle.setText(data.getSupergroup_Name());

        Glide.with(mContext).load(data.getSupergroup_Image()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Utils.setPrefData(CATEGORY_NAME, data.getCatName(), mContext);
//                Utils.setPrefData(CATEGORY_ID, data.getCatId(), mContext);
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("id",data.getSupergroup_Id());
                intent.putExtra("topicName",data.getFirebase_topic_name());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        AppCompatTextView tvTitle;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }
}
