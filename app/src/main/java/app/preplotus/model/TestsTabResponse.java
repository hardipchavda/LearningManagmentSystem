package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestsTabResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("offer_image")
    @Expose
    private String offer_image;

    @SerializedName("mock_test")
    @Expose
    private ArrayList<PackageData> mock_test;

    @SerializedName("latest_test")
    @Expose
    private ArrayList<ExamData> latest_test;

    @SerializedName("subject_list")
    @Expose
    private ArrayList<NotesData> subject_list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PackageData> getMock_test() {
        return mock_test;
    }

    public void setMock_test(ArrayList<PackageData> mock_test) {
        this.mock_test = mock_test;
    }

    public ArrayList<ExamData> getLatest_test() {
        return latest_test;
    }

    public void setLatest_test(ArrayList<ExamData> latest_test) {
        this.latest_test = latest_test;
    }

    public ArrayList<NotesData> getSubject_list() {
        return subject_list;
    }

    public void setSubject_list(ArrayList<NotesData> subject_list) {
        this.subject_list = subject_list;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }
}
