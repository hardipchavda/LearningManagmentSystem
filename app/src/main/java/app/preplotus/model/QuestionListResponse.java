package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionListResponse {

    @SerializedName("QuestionList")
    @Expose
    private ArrayList<QuestionsData> QuestionList;

    public ArrayList<QuestionsData> getQuestionList() {
        return QuestionList;
    }

    public void setQuestionList(ArrayList<QuestionsData> questionList) {
        QuestionList = questionList;
    }
}
