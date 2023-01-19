package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinsData {

    @SerializedName("CoinAmount")
    @Expose
    private String CoinAmount;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Earn From")
    @Expose
    private String from;

    public String getCoinAmount() {
        return CoinAmount;
    }

    public void setCoinAmount(String coinAmount) {
        CoinAmount = coinAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
