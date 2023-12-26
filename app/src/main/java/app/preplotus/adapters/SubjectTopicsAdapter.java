package app.preplotus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import app.preplotus.R;
import app.preplotus.activities.TestsActivity;
import app.preplotus.model.SubjectTopicData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectTopicsAdapter extends RecyclerView.Adapter<SubjectTopicsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<SubjectTopicData> subjectTopicData;

    public SubjectTopicsAdapter(Context context, ArrayList<SubjectTopicData> subjectTopicData) {
        this.mContext = context;
        this.subjectTopicData = subjectTopicData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_subject_topics, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {


        final SubjectTopicData n = subjectTopicData.get(position);
        holder.dis.setText(n.getTitle());
//        holder.iamge.setImageResource(n.getImage());

        Glide.with(mContext).load(n.getImage()).into(holder.iamge);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestsActivity.class);
                intent.putExtra("id", n.getId());
                intent.putExtra("title", n.getTitle());
                intent.putExtra("type", "topic");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectTopicData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dis;
        LinearLayout ll;
        ImageView iamge;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dis = itemView.findViewById(R.id.dis);
            iamge = itemView.findViewById(R.id.image);
            ll = itemView.findViewById(R.id.ll);

        }
    }


}
