package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyResultsResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("FetchResults")
    @Expose
    private ArrayList<MyResultData> FetchResults;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MyResultData> getFetchResults() {
        return FetchResults;
    }

    public void setFetchResults(ArrayList<MyResultData> fetchResults) {
        FetchResults = fetchResults;
    }
}
