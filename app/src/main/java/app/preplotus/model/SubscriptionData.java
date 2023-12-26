package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionData {

    @SerializedName("plan_id")
    @Expose
    private String plan_id;

    @SerializedName("validity_id")
    @Expose
    private String validity_id;

    @SerializedName("validity_name")
    @Expose
    private String validity_name;

    @SerializedName("start_date")
    @Expose
    private String start_date;

    @SerializedName("end_date")
    @Expose
    private String end_date;

    @SerializedName("supergroup_id")
    @Expose
    private String supergroup_id;

    @SerializedName("supergroup_name")
    @Expose
    private String supergroup_name;

    @SerializedName("payment_status")
    @Expose
    private String payment_status;

    @SerializedName("is_active")
    @Expose
    private String is_active;

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getValidity_id() {
        return validity_id;
    }

    public void setValidity_id(String validity_id) {
        this.validity_id = validity_id;
    }

    public String getValidity_name() {
        return validity_name;
    }

    public void setValidity_name(String validity_name) {
        this.validity_name = validity_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSupergroup_id() {
        return supergroup_id;
    }

    public void setSupergroup_id(String supergroup_id) {
        this.supergroup_id = supergroup_id;
    }

    public String getSupergroup_name() {
        return supergroup_name;
    }

    public void setSupergroup_name(String supergroup_name) {
        this.supergroup_name = supergroup_name;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
