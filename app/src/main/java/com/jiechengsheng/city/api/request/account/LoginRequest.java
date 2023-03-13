package com.jiechengsheng.city.api.request.account;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/9 2:32 下午
 */
public class LoginRequest {

    /**
     * type : 2
     * phone : 18667007550
     * verification_code : 1234
     * password : 123456
     */

    /**
     * 登录类型（1：手机验证码，2：手机密码）
     */
    @SerializedName("type")
    private int type;
    @SerializedName("phone")
    private String phone;
    @SerializedName("verification_code")
    private String verificationCode;
    @SerializedName("password")
    private String password;

    /**
     * 手机验证码
     */
    public final int TYPE_LOGIN_VERIFICATION_CODE = 1;
    /**
     * 账号加密码
     */
    public final int TYPE_LOGIN_PASSWORD = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
