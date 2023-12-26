package app.preplotus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.fragments.SolutionFragment;
import app.preplotus.model.SolutionData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuestionPalletAdapter extends RecyclerView.Adapter<QuestionPalletAdapter.QuestionViewHolder> {

    Context mContext;
    ArrayList<SolutionData> list;
    private SolutionFragment solutionFragment;

    public QuestionPalletAdapter(Context mContext,SolutionFragment solutionFragment, ArrayList<SolutionData> list) {
        this.mContext = mContext;
        this.list = list;
        this.solutionFragment = solutionFragment;
    }

    @NonNull
    @NotNull
    @Override
    public QuestionPalletAdapter.QuestionViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemquestion, parent, false);

        return new QuestionPalletAdapter.QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QuestionPalletAdapter.QuestionViewHolder holder, int position) {

        try {
           holder.tvNumber.setText("" + (position + 1));
           final SolutionData data = list.get(position);
           holder.tvNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white));
           holder.imgSymbol.setVisibility(View.GONE);
           if (data.getIsanswered().equals("1")) {
               holder.imgSymbol.setImageResource(R.drawable.icon_answered);
               holder.tvNumber.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
           } else if (data.getMarkedforreview().equals("1")) {
               holder.imgSymbol.setVisibility(View.VISIBLE);
               holder.imgSymbol.setImageResource(R.drawable.icon_mark_for_review);
               holder.tvNumber.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey3));
               holder.tvNumber.setTextColor(ContextCompat.getColor(mContext, R.color.black));
           } else if (!data.getIsvisited().equals("1")) {
               holder.imgSymbol.setImageResource(R.drawable.icon_not_visiting);
               holder.tvNumber.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
           } else {
               holder.imgSymbol.setImageResource(R.drawable.cross);
               holder.tvNumber.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
           }
           holder.imgSymbol.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   solutionFragment.showQuestion(holder.getAdapterPosition());
               }
           });
       } catch (Exception e){
           e.printStackTrace();
       }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvNumber;
        ImageView imgSymbol;

        public QuestionViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            imgSymbol = itemView.findViewById(R.id.imgSymbol);
        }
    }
}
