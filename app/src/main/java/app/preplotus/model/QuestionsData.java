package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionsData {

    @SerializedName("Question_id")
    @Expose
    private String Question_id;

    @SerializedName("Title")
    @Expose
    private String Title;

    private String isanswered="";
    private String isvisited="";
    private String markedforreview="";
    private String useranswerid="";

    @SerializedName("CorrectAnswer")
    @Expose
    private String CorrectAnswer;

    @SerializedName("Marks")
    @Expose
    private String Marks;

    @SerializedName("Question_Image")
    @Expose
    private String Question_Image;

    @SerializedName("option_data")
    @Expose
    private ArrayList<String> option_data;

    @SerializedName("ans_images")
    @Expose
    private ArrayList<String> ans_images;

    public String getQuestion_id() {
        return Question_id;
    }

    public void setQuestion_id(String question_id) {
        Question_id = question_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }

    public ArrayList<String> getOption_data() {
        return option_data;
    }

    public void setOption_data(ArrayList<String> option_data) {
        this.option_data = option_data;
    }

    public String getIsanswered() {
        return isanswered;
    }

    public void setIsanswered(String isanswered) {
        this.isanswered = isanswered;
    }

    public String getIsvisited() {
        return isvisited;
    }

    public void setIsvisited(String isvisited) {
        this.isvisited = isvisited;
    }

    public String getMarkedforreview() {
        return markedforreview;
    }

    public void setMarkedforreview(String markedforreview) {
        this.markedforreview = markedforreview;
    }

    public String getUseranswerid() {
        return useranswerid;
    }

    public void setUseranswerid(String useranswerid) {
        this.useranswerid = useranswerid;
    }

    public String getQuestion_Image() {
        return Question_Image;
    }

    public void setQuestion_Image(String question_Image) {
        Question_Image = question_Image;
    }

    public ArrayList<String> getAns_images() {
        return ans_images;
    }

    public void setAns_images(ArrayList<String> ans_images) {
        this.ans_images = ans_images;
    }
}
