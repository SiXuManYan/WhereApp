package com.jcs.where.api.request.modify;

/**
 * Created by Wangsw  2021/2/4 15:50.
 * 修改密码
 */
public class ModifyPasswordRequest {

    public String old_password = "";
    public String new_password = "";


    public static final class Builder {
        public String old_password = "";
        public String new_password = "";

        private Builder() {
        }

        public static Builder aModifyPasswordRequest() {
            return new Builder();
        }

        public Builder old_password(String old_password) {
            this.old_password = old_password;
            return this;
        }

        public Builder new_password(String new_password) {
            this.new_password = new_password;
            return this;
        }

        public ModifyPasswordRequest build() {
            ModifyPasswordRequest modifyPasswordRequest = new ModifyPasswordRequest();
            modifyPasswordRequest.new_password = this.new_password;
            modifyPasswordRequest.old_password = this.old_password;
            return modifyPasswordRequest;
        }
    }
}
