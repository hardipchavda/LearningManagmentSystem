package com.icss.learningmanagmentsystem.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.icss.learningmanagmentsystem.R;

import com.icss.learningmanagmentsystem.activities.QuestionActivity;
import com.icss.learningmanagmentsystem.activities.ResultsActivity;
import com.icss.learningmanagmentsystem.model.QuestionsData;

import java.util.ArrayList;

import butterknife.BindView;


public class CustomDialog extends Dialog {
    TextView cancle, yes;
    ArrayList<QuestionsData> listQuestions;

    AppCompatTextView tvNotVisited;
    AppCompatTextView tvAnswered;
    AppCompatTextView tvNotAnswered;
    AppCompatTextView tvMarkedForReview;
    private Context mContext;

    public CustomDialog(@NonNull Context context, ArrayList<QuestionsData> list) {
        super(context);
        this.mContext = context;
        this.listQuestions = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dialog);
        cancle = findViewById(R.id.cancel);
        yes = findViewById(R.id.yes);
        tvNotVisited = findViewById(R.id.tvNotVisited);
        tvAnswered = findViewById(R.id.tvAnswered);
        tvNotAnswered = findViewById(R.id.tvNotAnswered);
        tvMarkedForReview = findViewById(R.id.tvMarkedForReview);

        int cans = 0, cnotans = 0, cnotvis = 0, cmarkd = 0;
        for (int i = 0; i < listQuestions.size(); i++) {
            final QuestionsData data = listQuestions.get(i);
            if (data.getIsanswered().equals("1")) {
                cans = cans + 1;
            } else if (data.getMarkedforreview().equals("1")) {
                cmarkd = cmarkd + 1;
            } else if (!data.getIsvisited().equals("1")) {
                cnotvis = cnotvis + 1;
            } else {
                cnotans = cnotans + 1;
            }
        }
        tvAnswered.setText("Answered (" + cans + ")");
        tvNotVisited.setText("Not Visited (" + cnotvis + ")");
        tvNotAnswered.setText("Not Answered (" + cnotans + ")");
        tvMarkedForReview.setText("Marked For Review (" + cmarkd + ")");

        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((QuestionActivity) mContext).onFinishApiCall();
            }
        });


    }
}