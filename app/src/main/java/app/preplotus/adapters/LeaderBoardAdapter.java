package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.model.LeaderBoardData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    Context mContext;
    ArrayList<LeaderBoardData> leaderBoardData;

    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoardData> leaderBoardData) {
        this.mContext = context;
        this.leaderBoardData = leaderBoardData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_leaderboard, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        final LeaderBoardData data = leaderBoardData.get(position);

        holder.tvUserName.setText(data.getUsername());
        holder.tvMarks.setText(data.getGotMarks());

    }

    @Override
    public int getItemCount() {
        return leaderBoardData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvUserName, tvMarks;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvMarks = itemView.findViewById(R.id.tvMarks);
        }
    }

}
