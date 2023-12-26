package app.preplotus.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginSignupResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private LoginUserData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginUserData getData() {
        return data;
    }

    public void setData(LoginUserData data) {
        this.data = data;
    }
}
