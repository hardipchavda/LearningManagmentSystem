package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanData {

    @SerializedName("validity_id")
    @Expose
    private String validity_id;

    @SerializedName("validity_value")
    @Expose
    private String validity_value;

    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    @SerializedName("plan_discount")
    @Expose
    private String plan_discount;

    @SerializedName("plan_amount")
    @Expose
    private String plan_amount;

    @SerializedName("plan_amount_type")
    @Expose
    private String plan_amount_type;

    public String getValidity_id() {
        return validity_id;
    }

    public void setValidity_id(String validity_id) {
        this.validity_id = validity_id;
    }

    public String getValidity_value() {
        return validity_value;
    }

    public void setValidity_value(String validity_value) {
        this.validity_value = validity_value;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPlan_discount() {
        return plan_discount;
    }

    public void setPlan_discount(String plan_discount) {
        this.plan_discount = plan_discount;
    }

    public String getPlan_amount() {
        return plan_amount;
    }

    public void setPlan_amount(String plan_amount) {
        this.plan_amount = plan_amount;
    }

    public String getPlan_amount_type() {
        return plan_amount_type;
    }

    public void setPlan_amount_type(String plan_amount_type) {
        this.plan_amount_type = plan_amount_type;
    }
}
