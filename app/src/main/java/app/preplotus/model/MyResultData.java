package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyResultData {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("EXAMID")
    @Expose
    private String EXAMID;

    @SerializedName("exam_name")
    @Expose
    private String exam_name;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("gotMarks")
    @Expose
    private String gotMarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEXAMID() {
        return EXAMID;
    }

    public void setEXAMID(String EXAMID) {
        this.EXAMID = EXAMID;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGotMarks() {
        return gotMarks;
    }

    public void setGotMarks(String gotMarks) {
        this.gotMarks = gotMarks;
    }

    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

}
