package app.preplotus.modelclass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    public LoginDataModel data;
    @SerializedName("responce")
    @Expose
    public Boolean responce;
}
