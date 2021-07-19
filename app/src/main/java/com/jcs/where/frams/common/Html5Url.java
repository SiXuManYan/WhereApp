package com.jcs.where.frams.common;

import com.jcs.where.BuildConfig;

/**
 * Created by Wangsw  2021/1/29 10:11.
 * app内 H5 常量
 */
public class Html5Url {

    /**
     * 用户协议
     */
    public static final String USER_AGREEMENT_URL = BuildConfig.SERVER_HOST + "users/agreement?lang=%1$s";

    /**
     * 隐私政策
     */
    public static final String PRIVACY_POLICY = BuildConfig.SERVER_HOST + "privacy/agreement?lang=%1$s";

    /**
     * 条款和条件
     */
    public static final String CONDITION_AGREEMENT = BuildConfig.SERVER_HOST + "term_condition/agreement?lang=%1$s";

    /**
     * comm100 客服聊天地址
     */
    public static final String COMM100_CHAT_URL = "https://vue.livelyhelp.chat/chatWindow.aspx?siteId=60001617&planId=8da6d622-e5b2-4741-bcdc-855d5d82f95a#";
    public static final String MODEL_HOTEL = "hotel";
    public static final String MODEL_NEWS = "news";
    public static final String MODEL_TRAVEL = "travel";
    public static final String MODEL_GENERAL = "general";
    public static final String MODEL_RESTAURANT = "restaurant";
    public static final String MODEL_E_STORE = "eStore";


    /**
     * 分享facebook
     * 模块（hotel：酒店，news：新闻，travel：旅游景点，general：综合服务，restaurant：餐厅）
     */
    public static final String SHARE_FACEBOOK = BuildConfig.SERVER_HOST + "share/facebook?module=%1$s&id=%2$s&device=android";

    /**
     * 旅游管理 URL
     */
    public static final String TOURISM_MANAGEMENT_URL = "https://www.beholdbataan.ph/";


}
