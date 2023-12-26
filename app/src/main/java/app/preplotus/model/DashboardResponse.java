package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("NotesData")
    @Expose
    private ArrayList<NotesData> NotesData;

    @SerializedName("examData")
    @Expose
    private ArrayList<ExamData> examData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<app.preplotus.model.NotesData> getNotesData() {
        return NotesData;
    }

    public void setNotesData(ArrayList<app.preplotus.model.NotesData> notesData) {
        NotesData = notesData;
    }

    public ArrayList<ExamData> getExamData() {
        return examData;
    }

    public void setExamData(ArrayList<ExamData> examData) {
        this.examData = examData;
    }
}
