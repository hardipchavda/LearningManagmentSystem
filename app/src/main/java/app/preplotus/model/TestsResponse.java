package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestsResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("examData")
    @Expose
    private ArrayList<ExamData> examData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ArrayList<ExamData> getExamData() {
        return examData;
    }

    public void setExamData(ArrayList<ExamData> examData) {
        this.examData = examData;
    }
}
