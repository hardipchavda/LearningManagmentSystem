package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SolutionData {

    @SerializedName("que_id")
    @Expose
    private String que_id;

    @SerializedName("que_title")
    @Expose
    private String que_title;

    @SerializedName("answer_array")
    @Expose
    private ArrayList<String> answer_array;

    @SerializedName("correct_answer_id")
    @Expose
    private String correct_answer_id;

    @SerializedName("max_marks")
    @Expose
    private String max_marks;

    @SerializedName("user_selected_id")
    @Expose
    private String user_selected_id;

    @SerializedName("isvisited")
    @Expose
    private String isvisited;

    @SerializedName("isanswered")
    @Expose
    private String isanswered;

    @SerializedName("markedforreview")
    @Expose
    private String markedforreview;

    @SerializedName("scored_marks")
    @Expose
    private String scored_marks;

    @SerializedName("solution")
    @Expose
    private String solution;

    @SerializedName("ans_images")
    @Expose
    private ArrayList<String> ans_images;

    @SerializedName("que_image")
    @Expose
    private String Question_Image;

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    public String getQue_title() {
        return que_title;
    }

    public void setQue_title(String que_title) {
        this.que_title = que_title;
    }

    public ArrayList<String> getAnswer_array() {
        return answer_array;
    }

    public void setAnswer_array(ArrayList<String> answer_array) {
        this.answer_array = answer_array;
    }

    public String getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public String getMax_marks() {
        return max_marks;
    }

    public void setMax_marks(String max_marks) {
        this.max_marks = max_marks;
    }

    public String getUser_selected_id() {
        return user_selected_id;
    }

    public void setUser_selected_id(String user_selected_id) {
        this.user_selected_id = user_selected_id;
    }

    public String getIsvisited() {
        return isvisited;
    }

    public void setIsvisited(String isvisited) {
        this.isvisited = isvisited;
    }

    public String getIsanswered() {
        return isanswered;
    }

    public void setIsanswered(String isanswered) {
        this.isanswered = isanswered;
    }

    public String getMarkedforreview() {
        return markedforreview;
    }

    public void setMarkedforreview(String markedforreview) {
        this.markedforreview = markedforreview;
    }

    public String getScored_marks() {
        return scored_marks;
    }

    public void setScored_marks(String scored_marks) {
        this.scored_marks = scored_marks;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ArrayList<String> getAns_images() {
        return ans_images;
    }

    public void setAns_images(ArrayList<String> ans_images) {
        this.ans_images = ans_images;
    }

    public String getQuestion_Image() {
        return Question_Image;
    }

    public void setQuestion_Image(String question_Image) {
        Question_Image = question_Image;
    }
}
