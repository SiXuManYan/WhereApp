package com.jcs.where.api.request;

/**
 * Created by Wangsw  2021/1/29 14:34.
 * 注册
 */
public class RegisterRequest {

    public String phone = "";
    public String password = "";
    /**
     * 国家码
     */
    public String country_code = "";

    public String verification_code = "";

    public String invite_code = "";


    public static final class Builder {
        public String phone = "";
        public String password = "";
        public String country_code = "";
        public String verification_code = "";
        public String invite_code = "";

        private Builder() {
        }

        public static Builder aRegisterRequest() {
            return new Builder();
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
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

        public Builder invite_code(String invite_code) {
            this.invite_code = invite_code;
            return this;
        }

        public RegisterRequest build() {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.verification_code = this.verification_code;
            registerRequest.phone = this.phone;
            registerRequest.invite_code = this.invite_code;
            registerRequest.password = this.password;
            registerRequest.country_code = this.country_code;
            return registerRequest;
        }
    }
}
