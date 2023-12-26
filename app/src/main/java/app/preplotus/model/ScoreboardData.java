package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreboardData {

    @SerializedName("Totalmarks")
    @Expose
    private String Totalmarks;

    @SerializedName("gotMarks")
    @Expose
    private String gotMarks;

    @SerializedName("percentage")
    @Expose
    private String percentage;

    @SerializedName("totalrank")
    @Expose
    private String totalrank;

    @SerializedName("gotRank")
    @Expose
    private String gotRank;

    @SerializedName("attempted_que")
    @Expose
    private String attempted_que;

    @SerializedName("left_que")
    @Expose
    private String left_que;

    @SerializedName("correct_que")
    @Expose
    private String correct_que;

    @SerializedName("incorrect_que")
    @Expose
    private String incorrect_que;

    @SerializedName("correct_marks")
    @Expose
    private String correct_marks;

    @SerializedName("negative_marks")
    @Expose
    private String negative_marks;

    @SerializedName("total_time_taken")
    @Expose
    private String total_time_taken;

    public String getTotalmarks() {
        return Totalmarks;
    }

    public void setTotalmarks(String totalmarks) {
        Totalmarks = totalmarks;
    }

    public String getGotMarks() {
        return gotMarks;
    }

    public void setGotMarks(String gotMarks) {
        this.gotMarks = gotMarks;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotalrank() {
        return totalrank;
    }

    public void setTotalrank(String totalrank) {
        this.totalrank = totalrank;
    }

    public String getGotRank() {
        return gotRank;
    }

    public void setGotRank(String gotRank) {
        this.gotRank = gotRank;
    }

    public String getAttempted_que() {
        return attempted_que;
    }

    public void setAttempted_que(String attempted_que) {
        this.attempted_que = attempted_que;
    }

    public String getLeft_que() {
        return left_que;
    }

    public void setLeft_que(String left_que) {
        this.left_que = left_que;
    }

    public String getCorrect_que() {
        return correct_que;
    }

    public void setCorrect_que(String correct_que) {
        this.correct_que = correct_que;
    }

    public String getIncorrect_que() {
        return incorrect_que;
    }

    public void setIncorrect_que(String incorrect_que) {
        this.incorrect_que = incorrect_que;
    }

    public String getCorrect_marks() {
        return correct_marks;
    }

    public void setCorrect_marks(String correct_marks) {
        this.correct_marks = correct_marks;
    }

    public String getNegative_marks() {
        return negative_marks;
    }

    public void setNegative_marks(String negative_marks) {
        this.negative_marks = negative_marks;
    }

    public String getTotal_time_taken() {
        return total_time_taken;
    }

    public void setTotal_time_taken(String total_time_taken) {
        this.total_time_taken = total_time_taken;
    }

}
