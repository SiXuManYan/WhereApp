package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Wangsw  2021/1/23 16:56.
 */
public class SignListResponse {

    /**
     * data : [{"sign_status":"签到状态（1：已签到，2：未签到）","date":"日期","integral":"签到获得积分"}]
     * sign_days : 连续7天签到天数
     * total_sign_days : 总连续签到天数
     */

    /**
     * 连续7天签到天数
     */
    @SerializedName("sign_days")
    private String signDays;

    /**
     * 总连续签到天数
     */
    @SerializedName("total_sign_days")
    private String totalSignDays;

    @SerializedName("data")
    private List<DataBean> data;

    public String getSignDays() {
        return signDays;
    }

    public void setSignDays(String signDays) {
        this.signDays = signDays;
    }

    public String getTotalSignDays() {
        return totalSignDays;
    }

    public void setTotalSignDays(String totalSignDays) {
        this.totalSignDays = totalSignDays;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sign_status : 签到状态（1：已签到，2：未签到）
         * date : 日期
         * integral : 签到获得积分
         */

        /**
         * 	签到状态（1：已签到，2：未签到）
         */
        @SerializedName("sign_status")
        private int signStatus;


        @SerializedName("date")
        private String date;

        /**
         * 	签到获得积分
         */
        @SerializedName("integral")
        private String integral;

        public int getSignStatus() {
            return signStatus;
        }

        public void setSignStatus(int signStatus) {
            this.signStatus = signStatus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }
    }
}
