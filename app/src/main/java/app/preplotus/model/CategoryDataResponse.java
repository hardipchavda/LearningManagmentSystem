package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryDataResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private ArrayList<CategoryData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CategoryData> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryData> data) {
        this.data = data;
    }
}
