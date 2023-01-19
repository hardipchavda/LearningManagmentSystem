package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScorecardResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ScoreCarddata")
    @Expose
    ScoreboardData ScoreCarddata;

    @SerializedName("LeaderBoardData")
    @Expose
    ArrayList<LeaderBoardData> LeaderBoardData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ScoreboardData getScoreCarddata() {
        return ScoreCarddata;
    }

    public void setScoreCarddata(ScoreboardData scoreCarddata) {
        ScoreCarddata = scoreCarddata;
    }

    public ArrayList<app.preplotus.model.LeaderBoardData> getLeaderBoardData() {
        return LeaderBoardData;
    }

    public void setLeaderBoardData(ArrayList<app.preplotus.model.LeaderBoardData> leaderBoardData) {
        LeaderBoardData = leaderBoardData;
    }
}
