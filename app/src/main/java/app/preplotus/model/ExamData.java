package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("questions")
    @Expose
    private String questions;

    @SerializedName("marks")
    @Expose
    private String marks;

    @SerializedName("is_paid")
    @Expose
    private String is_paid;

    @SerializedName("is_attempted")
    @Expose
    private String is_attempted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getIs_attempted() {
        return is_attempted;
    }

    public void setIs_attempted(String is_attempted) {
        this.is_attempted = is_attempted;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }
}
