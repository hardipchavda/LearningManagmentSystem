package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyCoinsResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("CoinsBalance")
    @Expose
    private String CoinsBalance;

    @SerializedName("UsedCoins")
    @Expose
    private String UsedCoins;

    @SerializedName("CoinsValue")
    @Expose
    private String CoinsValue;

    @SerializedName("CoinsHistory")
    @Expose
    private ArrayList<CoinsData> CoinsHistory;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoinsBalance() {
        return CoinsBalance;
    }

    public void setCoinsBalance(String coinsBalance) {
        CoinsBalance = coinsBalance;
    }

    public String getUsedCoins() {
        return UsedCoins;
    }

    public void setUsedCoins(String usedCoins) {
        UsedCoins = usedCoins;
    }

    public String getCoinsValue() {
        return CoinsValue;
    }

    public void setCoinsValue(String coinsValue) {
        CoinsValue = coinsValue;
    }

    public ArrayList<CoinsData> getCoinsHistory() {
        return CoinsHistory;
    }

    public void setCoinsHistory(ArrayList<CoinsData> coinsHistory) {
        CoinsHistory = coinsHistory;
    }
}
