package com.jcs.where.api.request.modify;

/**
 * Created by Wangsw  2021/2/4 16:44.\
 * 修改手机号
 */
public class ModifyPhoneRequest {

    public String phone = "";
    public String country_code = "";


    public static final class Builder {
        public String phone = "";
        public String country_code = "";

        private Builder() {
        }

        public static Builder aModifyPhoneRequest() {
            return new Builder();
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder country_code(String country_code) {
            this.country_code = country_code;
            return this;
        }

        public ModifyPhoneRequest build() {
            ModifyPhoneRequest modifyPhoneRequest = new ModifyPhoneRequest();
            modifyPhoneRequest.country_code = this.country_code;
            modifyPhoneRequest.phone = this.phone;
            return modifyPhoneRequest;
        }
    }
}
