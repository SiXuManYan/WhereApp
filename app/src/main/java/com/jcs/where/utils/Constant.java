package com.jcs.where.utils;

/**
 * 常量
 * create by zyf on 2021/1/8 10:59 上午
 */
public class Constant {
    public static final String GOOGLE_PLAY_APP_STORE_PACKAGE_NAME = "com.android.vending";

    public static final double LAT = 14.6631685;
    public static final double LNG = 120.5887840;

    /**
     * 选取附件
     */
    public static final int REQUEST_MEDIA = 1001;

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
    public static final String PARAM_DISMISS_BACK_ICON = "dismiss_back_icon";
    public static final String PARAM_TITLE = "param_title";
    public static final String PARAM_CONTENT = "param_content";
    public static final String PARAM_CREATE_DATE = "param_create_date";
    public static final String PARAM_NEW_VERSION_CODE = "NEW_VERSION_CODE";
    public static final String PARAM_DOWNLOAD_URL = "DOWNLOAD_URL";
    public static final String PARAM_UPDATE_DESC = "UPDATE_DESC";
    public static final String PARAM_IS_FORCE_INSTALL = "IS_FORCE_INSTALL";
    public static final String PARAM_HOTEL_TYPE_IDS = "hotelTypeIds";


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
     * 验证码类型 3 忘记密码
     */
    public static final int VERIFY_CODE_TYPE_3_FORGET_PASSWORD = 3;


    /**
     * 验证码类型 4 更换手机号
     */
    public static final int VERIFY_CODE_TYPE_4_CHANGE_PHONE = 4;

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


    /**
     * 验证页面使用类型
     * 0 验证旧版手机号
     * 1 验证新手机号
     */
    public static final String PARAM_VERIFY_USE_MODE = "param_verify_use_mode";


    public static final int IMG = 1;
    public static final int GIF = 2;
    public static final int VIDEO = 3;

    public static final String PR_DEFAULT = "where";
    public static final String SP_SEARCH_HISTORY = "sp_search_history";


}
