package com.jiechengsheng.city.storage.entity;

import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.blankj.utilcode.util.Utils;
import com.jiechengsheng.city.BaseApplication;
import com.jiechengsheng.city.utils.CacheUtil;

/**
 * Created by Wangsw  2021/2/3 10:51.
 * 用户数据库
 * @see com.jiechengsheng.city.storage.Converters 类型转换
 */
@Entity(tableName = "table_user")
public class User {


    private static User instance;

    public static synchronized User getInstance() {
        if (instance == null) {
            BaseApplication app = (BaseApplication) Utils.getApp();
            instance = app.getDatabase().userDao().findUser();
            if (instance == null) {
                instance = new User();
            }
        }

        return User.instance;
    }

    public static boolean isLogon() {
        return !TextUtils.isEmpty(CacheUtil.getToken());
    }

    public static String getToken() {
        return CacheUtil.getToken();
    }

    public static void update() {
        instance = null;
    }

    public static void clearAllUser() {
        BaseApplication app = (BaseApplication) Utils.getApp();
        app.getDatabase().userDao().clear();
        instance = null;
    }


    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "nickname")
    public String nickName;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "avatar")
    public String avatar;

    /**
     * 余额
     */
    @ColumnInfo(name = "balance")
    public String balance;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @ColumnInfo(name = "name")
    public String name;


    /**
     * "type": "账户类型（1：账户体系，2：第三方体系）",
     */
    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "country_code")
    public String countryCode;

    /**
     * "商户入驻状态（1：已提交，2：未提交）",
     */
    @ColumnInfo(name = "merchant_apply_status")
    public int merchantApplyStatus;

    /**
     * "Facebook绑定状态（1：已绑定，2：未绑定）"
     */
    @ColumnInfo(name = "facebook_bind_status")
    public int faceBookBindStatus;

    /**
     * Google绑定状态（1：已绑定，2：未绑定）
     */
    @ColumnInfo(name = "google_bind_status")
    public int googleBindStatus;

    /**
     * twitter绑定状态（1：已绑定，2：未绑定）
     */
    @ColumnInfo(name = "twitter_bind_status")
    public int twitterBindStatus;

    /**
     * 签到状态（1：已签到，2：未签到）
     */
    @ColumnInfo(name = "sign_status")
    public int signStatus;

    @ColumnInfo(name = "integral")
    public String integral = "";

    /**
     * 用户融云数据
     */
    @ColumnInfo(name = "rong_data")
    public UserRongyunData rongData = new UserRongyunData();




    public static final class Builder {
        public long id;
        public String nickName;
        public String phone;
        public String email;
        public String avatar;
        public String balance;
        public String createdAt;
        public String name;
        public int type;
        public String countryCode;
        public int merchantApplyStatus;
        public int faceBookBindStatus;
        public int googleBindStatus;
        public int twitterBindStatus;
        public int signStatus;
        public String integral = "";
        public UserRongyunData rongData = new UserRongyunData();

        private Builder() {
        }

        public static Builder anUser() {
            return new Builder();
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder nickName(String nickName) {
            this.nickName = nickName;
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

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder balance(String balance) {
            this.balance = balance;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder merchantApplyStatus(int merchantApplyStatus) {
            this.merchantApplyStatus = merchantApplyStatus;
            return this;
        }

        public Builder faceBookBindStatus(int faceBookBindStatus) {
            this.faceBookBindStatus = faceBookBindStatus;
            return this;
        }

        public Builder googleBindStatus(int googleBindStatus) {
            this.googleBindStatus = googleBindStatus;
            return this;
        }

        public Builder twitterBindStatus(int twitterBindStatus) {
            this.twitterBindStatus = twitterBindStatus;
            return this;
        }

        public Builder signStatus(int signStatus) {
            this.signStatus = signStatus;
            return this;
        }

        public Builder integral(String integral) {
            this.integral = integral;
            return this;
        }

        public Builder rongData(UserRongyunData rongData) {
            this.rongData = rongData;
            return this;
        }

        public User build() {
            User user = new User();
            user.phone = this.phone;
            user.balance = this.balance;
            user.faceBookBindStatus = this.faceBookBindStatus;
            user.name = this.name;
            user.avatar = this.avatar;
            user.twitterBindStatus = this.twitterBindStatus;
            user.signStatus = this.signStatus;
            user.rongData = this.rongData;
            user.integral = this.integral;
            user.type = this.type;
            user.merchantApplyStatus = this.merchantApplyStatus;
            user.countryCode = this.countryCode;
            user.email = this.email;
            user.id = this.id;
            user.createdAt = this.createdAt;
            user.googleBindStatus = this.googleBindStatus;
            user.nickName = this.nickName;
            return user;
        }
    }
}
