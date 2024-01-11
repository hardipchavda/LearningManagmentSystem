package app.preplotus.adapters;

import static app.preplotus.utilities.Constants.SUBSCRIBED;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import app.preplotus.R;

import java.util.ArrayList;

import app.preplotus.activities.MyResultsActivity;
import app.preplotus.activities.ResultsActivity;
import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.model.ExamData;
import app.preplotus.activities.InstructionActivity;
import app.preplotus.utilities.Utils;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.viewHolder> {

    ArrayList<ExamData> examData;
    private Context mContext;
    private Dialog dialog;

    public TestsAdapter(Context mContext, ArrayList<ExamData> examData) {
        this.mContext = mContext;
        this.examData = examData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_latest_test, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder,int pos) {

        final int position = holder.getAdapterPosition();

        final ExamData data = examData.get(position);

        holder.abc_test_name.setText(data.getTitle());
//        holder.abc_test_name.setText("Hello this is long header name testing for home screen");
        holder.abc_test_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.abc_test_name.setSingleLine(true);
        holder.abc_test_name.setMarqueeRepeatLimit(50);
        holder.abc_test_name.setSelected(true);
        holder.time.setText(data.getTime()+" Min");
        holder.marks.setText(data.getMarks());
        holder.quetion.setText(data.getQuestions());

        if (data.getIs_attempted() != null && data.getIs_attempted().equals("1")) {
            holder.tvAttempt.setVisibility(View.VISIBLE);
        } else {
            holder.tvAttempt.setVisibility(View.GONE);
        }

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getIs_attempted() != null && data.getIs_attempted().equals("1")) {
                    openDialog(position);
                } else {
                    Intent intent = new Intent(mContext, InstructionActivity.class);
                    intent.putExtra("testid", data.getId());
                    intent.putExtra("title", data.getTitle());
                    intent.putExtra("marks", data.getMarks());
                    intent.putExtra("time", data.getTime());
                    intent.putExtra("type", "test");
                    mContext.startActivity(intent);
                }
            }
        });

        if (!data.getIs_paid().equals("free") && !Utils.getPrefData(SUBSCRIBED,mContext).equals("yes")) {
           holder.imgUnlock.setVisibility(View.VISIBLE);
           holder.btn.setVisibility(View.GONE);
            holder.imgUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(mContext, SubscriptionActivity.class);
                    mContext.startActivity(in);
                }
            });
        } else {
            holder.imgUnlock.setVisibility(View.GONE);
            holder.btn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return examData.size();
    }

    public void openDialog(final int pos) {

        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_attempt, null);

        AppCompatButton btnAttempt = rl.findViewById(R.id.btnAttempt);
        AppCompatTextView tvViewResult = rl.findViewById(R.id.tvViewResult);
        AppCompatTextView tvViewAllResult = rl.findViewById(R.id.tvViewAllResult);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                final ExamData data = examData.get(pos);
                Intent intent = new Intent(mContext, ResultsActivity.class);
                intent.putExtra("testid", data.getId());
                intent.putExtra("title", data.getTitle());
                intent.putExtra("type", "first");
                mContext.startActivity(intent);
            }
        });

        tvViewAllResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent in = new Intent(mContext, MyResultsActivity.class);
                mContext.startActivity(in);
            }
        });

        btnAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ExamData data = examData.get(pos);
                Intent intent = new Intent(mContext, InstructionActivity.class);
                intent.putExtra("testid", data.getId());
                intent.putExtra("title", data.getTitle());
                intent.putExtra("marks", data.getMarks());
                intent.putExtra("time", data.getTime());
                intent.putExtra("type", "practice");
                mContext.startActivity(intent);
            }
        });

        dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(rl);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView abc_test_name, tvAttempt;
        TextView time, marks, quetion;
        CardView btn;
        ImageView imgUnlock;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            abc_test_name = itemView.findViewById(R.id.heading);
            time = itemView.findViewById(R.id.time);
            marks = itemView.findViewById(R.id.mrk);
            quetion = itemView.findViewById(R.id.qus);
            btn = itemView.findViewById(R.id.btn);
            imgUnlock = itemView.findViewById(R.id.imgUnlock);
            tvAttempt = itemView.findViewById(R.id.tvAttempt);

        }
    }

}
