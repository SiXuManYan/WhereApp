package com.jcs.where.api.request;

/**
 * Created by Wangsw  2021/1/30 11:29.
 * 重置密码
 */
public class ResetPasswordRequest {

    /**
     * 类型（1：手机号）
     */
    public static final String TYPE_PHONE = "1";

    /**
     * 类型（2：邮箱）
     */
    public static final String TYPE_EMAIL = "2";

    /**
     * 类型（1：手机号，2：邮箱）
     */
    public String type = "";
    public String phone = "";
    public String email ;
    public String verification_code = "";
    public String password = "";


    public static final class Builder {
        public String type = "";
        public String phone = "";
        public String email = "";
        public String verification_code = "";
        public String password = "";

        private Builder() {
        }

        public static Builder aResetPasswordRequest() {
            return new Builder();
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
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

        public ResetPasswordRequest build() {
            ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
            resetPasswordRequest.phone = this.phone;
            resetPasswordRequest.password = this.password;
            resetPasswordRequest.type = this.type;
            resetPasswordRequest.verification_code = this.verification_code;
            resetPasswordRequest.email = this.email;
            return resetPasswordRequest;
        }
    }
}
