package app.preplotus.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotResponse {

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("responce")
    @Expose
    public Boolean responce;
    @SerializedName("message")
    @Expose
    public String message;
}
