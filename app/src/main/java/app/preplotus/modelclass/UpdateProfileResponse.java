package app.preplotus.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {
    @SerializedName("responce")
    @Expose
    public Boolean responce;
    @SerializedName("data")
    @Expose
    public UpadateProfileDataModel data;
}
