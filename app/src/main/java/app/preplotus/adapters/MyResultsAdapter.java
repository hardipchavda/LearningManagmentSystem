package app.preplotus.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;

import app.preplotus.activities.ResultsActivity;
import app.preplotus.model.MyResultData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyResultsAdapter extends RecyclerView.Adapter<MyResultsAdapter.viewHolder> {

    ArrayList<MyResultData> examData;
    private Context mContext;
    private Dialog dialog;
    private String type;

    public MyResultsAdapter(Context mContext, ArrayList<MyResultData> examData,String type) {
        this.mContext = mContext;
        this.examData = examData;
        this.type = type;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_my_results, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final MyResultData data = examData.get(position);

        holder.tvTitle.setText(data.getExam_name());
        try {

            String orgdate = data.getCreated_at().split(" ")[0];
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM yyyy");
            Date date = inputFormat.parse(orgdate);
            String str = outputFormat.format(date);
            holder.tvDate.setText(str);

        } catch (Exception e){}


        holder.tvSeeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResultsActivity.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("testid", data.getEXAMID());
                intent.putExtra("title", data.getExam_name());
                intent.putExtra("type", type);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return examData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvTitle, tvDate, tvSeeResult;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSeeResult = itemView.findViewById(R.id.tvSeeResult);

        }
    }


}
