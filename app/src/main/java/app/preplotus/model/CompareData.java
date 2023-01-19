package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompareData {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("rank")
    @Expose
    private String rank;

    @SerializedName("max_marks")
    @Expose
    private String max_marks;

    @SerializedName("attempted_que")
    @Expose
    private String attempted_que;

    @SerializedName("unattempted_que")
    @Expose
    private String unattempted_que;

    @SerializedName("totalQue")
    @Expose
    private String totalQue;

    @SerializedName("correct_que")
    @Expose
    private String correct_que;

    @SerializedName("incorrect_que")
    @Expose
    private String incorrect_que;

    @SerializedName("totalscrore")
    @Expose
    private String totalscrore;

    @SerializedName("percentage")
    @Expose
    private String percentage;

    @SerializedName("test_time")
    @Expose
    private String test_time;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMax_marks() {
        return max_marks;
    }

    public void setMax_marks(String max_marks) {
        this.max_marks = max_marks;
    }

    public String getAttempted_que() {
        return attempted_que;
    }

    public void setAttempted_que(String attempted_que) {
        this.attempted_que = attempted_que;
    }

    public String getUnattempted_que() {
        return unattempted_que;
    }

    public void setUnattempted_que(String unattempted_que) {
        this.unattempted_que = unattempted_que;
    }

    public String getTotalQue() {
        return totalQue;
    }

    public void setTotalQue(String totalQue) {
        this.totalQue = totalQue;
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

    public String getTotalscrore() {
        return totalscrore;
    }

    public void setTotalscrore(String totalscrore) {
        this.totalscrore = totalscrore;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTest_time() {
        return test_time;
    }

    public void setTest_time(String test_time) {
        this.test_time = test_time;
    }
}
