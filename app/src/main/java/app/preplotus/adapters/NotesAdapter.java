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
import app.preplotus.activities.TopicsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import app.preplotus.model.NotesData;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesDashboardViewHolder> {

    Context mContext;
    ArrayList<NotesData> notesDashboards;

    public NotesAdapter(Context context, ArrayList<NotesData> notesDashboards) {
        this.mContext = context;
        this.notesDashboards = notesDashboards;
    }

    @NonNull
    @NotNull
    @Override
    public NotesDashboardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemnotes, parent, false);
        return new NotesDashboardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotesDashboardViewHolder holder, int position) {


        final NotesData n = notesDashboards.get(position);
        holder.dis.setText(n.getNoteTitle());
//        holder.iamge.setImageResource(n.getImage());

        Glide.with(mContext).load(n.getNoteImage()).into(holder.iamge);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicsActivity.class);
                intent.putExtra("noteId",n.getNoteId());
                intent.putExtra("noteTitle",n.getNoteTitle());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesDashboards.size();
    }


    public class NotesDashboardViewHolder extends RecyclerView.ViewHolder {

        TextView dis;
        LinearLayout ll;
        ImageView iamge;

        public NotesDashboardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dis = itemView.findViewById(R.id.dis);
            iamge = itemView.findViewById(R.id.image);
            ll = itemView.findViewById(R.id.ll);

        }
    }


}
