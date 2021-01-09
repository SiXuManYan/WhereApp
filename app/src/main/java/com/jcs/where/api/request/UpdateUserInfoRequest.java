package com.jcs.where.api.request;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/10 12:34 上午
 */
public class UpdateUserInfoRequest {

    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private int avatar;

    public UpdateUserInfoRequest(String nickname) {
        this.nickname = nickname;
    }

    public UpdateUserInfoRequest(int avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
