package com.jiechengsheng.city.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/10 12:34 上午
 */
public class UpdateUserInfoRequest {

    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
