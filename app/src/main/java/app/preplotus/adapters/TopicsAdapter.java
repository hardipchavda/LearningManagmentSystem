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

import app.preplotus.R;
import app.preplotus.model.TopicData;
import app.preplotus.activities.SubTopicsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.SubjectNameViewHolder> {


    Context mContext;
    ArrayList<TopicData> topicList;

    public TopicsAdapter(Context context, ArrayList<TopicData> topicList) {
        this.mContext = context;
        this.topicList = topicList;
    }

    @NonNull
    @NotNull
    @Override
    public SubjectNameViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemsubjectname, parent, false);

        return new SubjectNameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubjectNameViewHolder holder, int position) {

        final TopicData modal = topicList.get(position);
        holder.tottlesubject.setText("Total Sub Topics: " + modal.getCount());
        holder.tittle.setText(modal.getTitle());

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, SubTopicsActivity.class);
                intent.putExtra("topicId", modal.getId());
                intent.putExtra("topicTitle", modal.getTitle());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }


    public class SubjectNameViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tittle, tottlesubject;
        LinearLayout ll;

        public SubjectNameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.topic);
            tottlesubject = itemView.findViewById(R.id.totalsubject);
            image = itemView.findViewById(R.id.image);
            ll = itemView.findViewById(R.id.ll);
        }

    }

}
