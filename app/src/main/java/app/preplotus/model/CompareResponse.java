package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CompareResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("Logged_user_data")
    @Expose
    private ArrayList<CompareData> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CompareData> getResults() {
        return results;
    }

    public void setResults(ArrayList<CompareData> results) {
        this.results = results;
    }
}
