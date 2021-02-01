package com.jcs.where.api.request.account;

/**
 * Created by Wangsw  2021/2/1 16:37.
 * 绑定手机号
 */
public class BindPhoneRequest {
    public static final int TYPE_FACEBOOK = 1;
    public static final int TYPE_GOOGLE = 2;
    public static final int TYPE_TWITTER = 3;


    /**
     * 登录类型（1：Facebook，2：Google，3：Twitter）
     */
    public int type = 0;

    /**
     * 第三方平台用户ID
     */
    public String open_id = "";

    /**
     * 昵称
     */
    public String nickname = "";

    /**
     * 头像
     */
    public String avatar = "";


    public String phone = "";
    public String country_code = "";
    public String verification_code = "";
    public String password = "";


    public static final class Builder {
        public int type = 0;
        public String open_id = "";
        public String nickname = "";
        public String avatar = "";
        public String phone = "";
        public String country_code = "";
        public String verification_code = "";
        public String password = "";

        private Builder() {
        }

        public static Builder aBindPhoneRequest() {
            return new Builder();
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder open_id(String open_id) {
            this.open_id = open_id;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder country_code(String country_code) {
            this.country_code = country_code;
            return this;
        }

        public Builder verification_code(String verification_code) {
            this.verification_code = verification_code;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public BindPhoneRequest build() {
            BindPhoneRequest bindPhoneRequest = new BindPhoneRequest();
            bindPhoneRequest.nickname = this.nickname;
            bindPhoneRequest.avatar = this.avatar;
            bindPhoneRequest.type = this.type;
            bindPhoneRequest.password = this.password;
            bindPhoneRequest.open_id = this.open_id;
            bindPhoneRequest.country_code = this.country_code;
            bindPhoneRequest.phone = this.phone;
            bindPhoneRequest.verification_code = this.verification_code;
            return bindPhoneRequest;
        }
    }
}
