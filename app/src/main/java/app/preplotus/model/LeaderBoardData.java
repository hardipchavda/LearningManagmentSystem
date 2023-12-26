package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaderBoardData {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("gotMarks")
    @Expose
    private String gotMarks;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGotMarks() {
        return gotMarks;
    }

    public void setGotMarks(String gotMarks) {
        this.gotMarks = gotMarks;
    }
}
