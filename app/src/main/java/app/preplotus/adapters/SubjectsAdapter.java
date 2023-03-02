package app.preplotus.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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

import app.preplotus.activities.SubjectTopicsActivity;
import app.preplotus.activities.TestsActivity;
import app.preplotus.model.NotesData;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.viewHolder> {

    ArrayList<NotesData> list;
    Context mContext;

    public SubjectsAdapter(Context context, ArrayList<NotesData> list) {
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_name_data_model_activity, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final NotesData data = list.get(position);
        Glide.with(mContext).load(data.getNoteImage()).into(holder.imageView);
        holder.textView.setText(data.getNoteTitle());
        holder.textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.textView.setSingleLine(true);
        holder.textView.setMarqueeRepeatLimit(20);
        holder.textView.setSelected(true);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (data.getNoteHasTopics()!=null && data.getNoteHasTopics().equals("1")){
                    Intent intent = new Intent(mContext, SubjectTopicsActivity.class);
                    intent.putExtra("id", data.getNoteId());
                    intent.putExtra("title", data.getNoteTitle());
                    intent.putExtra("type", "subject");
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, TestsActivity.class);
                    intent.putExtra("id", data.getNoteId());
                    intent.putExtra("title", data.getNoteTitle());
                    intent.putExtra("type", "subject");
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.txt1);
        }
    }
}
