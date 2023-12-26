package app.preplotus.adapters;

import static app.preplotus.utilities.Constants.SUBSCRIBED;

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
import app.preplotus.activities.ContentActivity;
import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.model.SubTopicData;
import app.preplotus.utilities.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubTopicsAdapter extends RecyclerView.Adapter<SubTopicsAdapter.TopicNameViewHolder> {

    Context mContext;
    ArrayList<SubTopicData> list;

    public SubTopicsAdapter(Context context, ArrayList<SubTopicData> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public TopicNameViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemtopicname, parent, false);
        return new TopicNameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopicNameViewHolder holder, int position) {
        final SubTopicData modal = list.get(position);
        holder.subTopic.setText(modal.getTitle());
        holder.totalPages.setText("Total Pages: " + modal.getCount());

        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContentActivity.class);
                intent.putExtra("type", "content");
                intent.putExtra("title", modal.getTitle());
                intent.putExtra("file_url", modal.getFile_url());
                intent.putExtra("file_path", modal.getFile_path());
                intent.putExtra("subtopicid", modal.getId());
                mContext.startActivity(intent);
            }
        });

        if (!modal.getIs_paid().equals("free") && !Utils.getPrefData(SUBSCRIBED,mContext).equals("yes")) {
            holder.imgUnlock.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.GONE);
            holder.imgUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, SubscriptionActivity.class);
                    mContext.startActivity(in);
                }
            });
        } else {
            holder.imgUnlock.setVisibility(View.GONE);
            holder.image2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TopicNameViewHolder extends RecyclerView.ViewHolder {
        ImageView image, image2, imgUnlock;
        TextView totalPages, subTopic;
        LinearLayout ll;


        public TopicNameViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            image2 = itemView.findViewById(R.id.image2);
            imgUnlock = itemView.findViewById(R.id.imgUnlock);
            totalPages = itemView.findViewById(R.id.totalPages);
            subTopic = itemView.findViewById(R.id.subTopic);
//            image2 = itemView.findViewById(R.id.image2);
            ll = itemView.findViewById(R.id.ll);

        }
    }
}



