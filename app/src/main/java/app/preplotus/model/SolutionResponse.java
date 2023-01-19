package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SolutionResponse {

    @SerializedName("Fetch_solution")
    @Expose
    ArrayList<SolutionData> Fetch_solution;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SolutionData> getFetch_solution() {
        return Fetch_solution;
    }

    public void setFetch_solution(ArrayList<SolutionData> fetch_solution) {
        Fetch_solution = fetch_solution;
    }

}
