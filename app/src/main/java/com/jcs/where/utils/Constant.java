package com.jcs.where.utils;

/**
 * 常量
 * create by zyf on 2021/1/8 10:59 上午
 */
public class Constant {

    public static final double LAT = 14.6631685;
    public static final double LNG = 120.5887840;

    /**
     * 列表接口"第一页"页码
     */
    public static final int DEFAULT_FIRST_PAGE = 1;

    public static final String PARAM_POSITION = "position";

    /**
     * 账号
     */
    public static final String PARAM_ACCOUNT = "account";

    /**
     * 验证码
     */
    public static final String PARAM_VERIFY_CODE = "verify_code";

    /**
     * 国际码
     */
    public static final String PARAM_COUNTRY_CODE = "country_code";
    public static final String PARAM_USER_NAME = "user_name";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_USER_ICON = "user_icon";
    /**
     * 第三方登录类型
     * （1：Facebook，2：google，3：Twitter）
     */
    public static final String PARAM_THREE_PARTY_LOGIN_TYPE = "three_party_login_type";

    /**
     * 验证码类型 1 登录
     */
    public static final int VERIFY_CODE_TYPE_1_LOGIN = 1;

    /**
     * 验证码类型 2 注册
     */
    public static final int VERIFY_CODE_TYPE_2_REGISTER = 2;

    /**
     * 验证码登录
     */
    public static final int LOGIN_TYPE_VERIFY_CODE = 1;

    /**
     * 密码登录
     */
    public static final int LOGIN_TYPE_PASSWORD = 2;


    /**
     * 验证码等待时间
     */
    public static final long WAIT_DELAYS = 60;


}
