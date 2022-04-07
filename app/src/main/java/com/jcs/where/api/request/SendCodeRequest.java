package com.jcs.where.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/9 2:55 下午
 */
public class SendCodeRequest {

    /**
     * phone : 18524110702
     * type : 1
     * country_code : 86
     */

    @SerializedName("phone")
    public String phone;


    /**
     * 发送类型（1：登录，2：注册，3：忘记密码，4：更换手机号）
     */
    @SerializedName("type")
    public int type;
    @SerializedName("country_code")
    public String countryCode;

    public final int TYPE_LOGIN = 1;
    public final int TYPE_REGISTER = 2;
    public final int TYPE_FORGET_PASSWORD = 3;
    public final int TYPE_CHANGE_PHONE_NUMBER = 4;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
