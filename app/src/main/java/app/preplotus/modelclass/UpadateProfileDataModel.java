package app.preplotus.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpadateProfileDataModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("firebase_id")
    @Expose
    public String firebaseId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("profile")
    @Expose
    public String profile;
    @SerializedName("fcm_id")
    @Expose
    public Object fcmId;
    @SerializedName("coins")
    @Expose
    public String coins;
    @SerializedName("refer_code")
    @Expose
    public String referCode;
    @SerializedName("friends_code")
    @Expose
    public Object friendsCode;
    @SerializedName("ip_address")
    @Expose
    public String ipAddress;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("date_registered")
    @Expose
    public String dateRegistered;
    @SerializedName("otp")
    @Expose
    public String otp;
}
