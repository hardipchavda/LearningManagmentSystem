package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.model.CompareData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {

    Context mContext;
    ArrayList<CompareData> list;

    public CompareAdapter(Context context, ArrayList<CompareData> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_compare, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {


        final CompareData data = list.get(position);

        holder.tvAttemptedQue.setText(data.getAttempted_que());
        holder.tvUnattemptedQue.setText(data.getUnattempted_que());
        holder.tvTotalScore.setText(data.getTotalscrore());
        holder.tvMaxMarks.setText(data.getMax_marks());
        holder.tvPercentage.setText(data.getPercentage());
        holder.tvCorrectQue.setText(data.getCorrect_que());
        holder.tvIncorrectQue.setText(data.getIncorrect_que());
        holder.tvTotalQue.setText(data.getTotalQue());
        holder.tvTotalTime.setText(data.getTest_time()+" min");

        if (position==0){
            holder.tvRank.setText("My Rank: "+data.getRank());
            holder.tvUserName.setText("Me");
        } else {
            holder.tvRank.setText("Rank: "+data.getRank());
            holder.tvUserName.setText(data.getUsername());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvUserName,tvRank,tvTotalQue,tvMaxMarks,tvAttemptedQue,
                tvUnattemptedQue,tvCorrectQue,tvIncorrectQue,tvTotalScore,tvPercentage,tvTotalTime;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvTotalQue = itemView.findViewById(R.id.tvTotalQue);
            tvMaxMarks = itemView.findViewById(R.id.tvMaxMarks);
            tvAttemptedQue = itemView.findViewById(R.id.tvAttemptedQue);
            tvUnattemptedQue = itemView.findViewById(R.id.tvUnattemptedQue);
            tvCorrectQue = itemView.findViewById(R.id.tvCorrectQue);
            tvIncorrectQue = itemView.findViewById(R.id.tvIncorrectQue);
            tvTotalScore = itemView.findViewById(R.id.tvTotalScore);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            tvTotalTime = itemView.findViewById(R.id.tvTotalTime);


        }
    }


}
