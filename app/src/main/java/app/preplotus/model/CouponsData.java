package app.preplotus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponsData {

    @SerializedName("coupon_code")
    @Expose
    private String coupon_code;

    @SerializedName("coupon_amount_or_discount")
    @Expose
    private String coupon_amount_or_discount;

    @SerializedName("minimum_order_value")
    @Expose
    private String minimum_order_value;

    @SerializedName("coupon_code_value")
    @Expose
    private String coupon_code_value;

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_amount_or_discount() {
        return coupon_amount_or_discount;
    }

    public void setCoupon_amount_or_discount(String coupon_amount_or_discount) {
        this.coupon_amount_or_discount = coupon_amount_or_discount;
    }

    public String getMinimum_order_value() {
        return minimum_order_value;
    }

    public void setMinimum_order_value(String minimum_order_value) {
        this.minimum_order_value = minimum_order_value;
    }

    public String getCoupon_code_value() {
        return coupon_code_value;
    }

    public void setCoupon_code_value(String coupon_code_value) {
        this.coupon_code_value = coupon_code_value;
    }
}
