package app.preplotus.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyProfileResponse {
    @SerializedName("data")
    @Expose
    public MyprofileDataModel data;
    @SerializedName("responce")
    @Expose
    public Boolean responce;
}
