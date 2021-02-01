package com.jcs.where.api.request.account;

/**
 * Created by Wangsw  2021/2/1 15:51.
 * 第三方登录请求
 */
public class ThreePartyLoginRequest {

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

    public static final class Builder {
        public int type = 0;
        public String open_id = "";
        public String nickname = "";
        public String avatar = "";

        private Builder() {
        }

        public static Builder aThreePartyLoginRequest() {
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

        public ThreePartyLoginRequest build() {
            ThreePartyLoginRequest threePartyLoginRequest = new ThreePartyLoginRequest();
            threePartyLoginRequest.avatar = this.avatar;
            threePartyLoginRequest.nickname = this.nickname;
            threePartyLoginRequest.type = this.type;
            threePartyLoginRequest.open_id = this.open_id;
            return threePartyLoginRequest;
        }
    }
}
