package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;
import com.jcs.where.storage.entity.UserRongyunData;

/**
 * create by zyf on 2021/1/9 11:29 下午
 */
public class UserInfoResponse {

    /**
     * id : 46
     * nickname : user_SmMc75
     * phone : 18524110702
     * country_code : 86
     * email :
     * avatar :
     * balance : 0
     * created_at : 2020-11-28 09:31:30
     * name : 5d631f77-20d9-4484-9244-b832b72e5df7
     * type : 1
     * merchant_apply_status : 2
     * facebook_bind_status : 2
     * google_bind_status : 2
     * twitter_bind_status : 2
     * integral : 500
     * sign_status : 2
     */

    @SerializedName("id")
    private Long id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("phone")
    private String phone;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("balance")
    private String balance;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private Integer type;
    @SerializedName("merchant_apply_status")
    private Integer merchantApplyStatus;
    @SerializedName("facebook_bind_status")
    private Integer facebookBindStatus;
    @SerializedName("google_bind_status")
    private Integer googleBindStatus;
    @SerializedName("twitter_bind_status")
    private Integer twitterBindStatus;
    @SerializedName("integral")
    private String integral = "0";
    @SerializedName("sign_status")
    private Integer signStatus;

    @SerializedName("rong_data")
    public UserRongyunData rongData;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMerchantApplyStatus() {
        return merchantApplyStatus;
    }

    public void setMerchantApplyStatus(Integer merchantApplyStatus) {
        this.merchantApplyStatus = merchantApplyStatus;
    }

    public Integer getFacebookBindStatus() {
        return facebookBindStatus;
    }

    public void setFacebookBindStatus(Integer facebookBindStatus) {
        this.facebookBindStatus = facebookBindStatus;
    }

    public Integer getGoogleBindStatus() {
        return googleBindStatus;
    }

    public void setGoogleBindStatus(Integer googleBindStatus) {
        this.googleBindStatus = googleBindStatus;
    }

    public Integer getTwitterBindStatus() {
        return twitterBindStatus;
    }

    public void setTwitterBindStatus(Integer twitterBindStatus) {
        this.twitterBindStatus = twitterBindStatus;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }
}
