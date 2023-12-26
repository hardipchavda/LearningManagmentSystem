package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubTopicsResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private ArrayList<SubTopicData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SubTopicData> getData() {
        return data;
    }

    public void setData(ArrayList<SubTopicData> data) {
        this.data = data;
    }
}
