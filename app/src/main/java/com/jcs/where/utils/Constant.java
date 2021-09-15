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
    public static final String PARAM_ID = "param_id";
    public static final String PARAM_TYPE = "param_type";
    public static final String PARAM_TAB = "param_tab";
    public static final String PARAM_CHANGE_LANGUAGE = "param_change_language";
    public static final String PARAM_ID_2 = "id";
    public static final String PARAM_PID = "param_pid";
    public static final String PARAM_PID_NAME = "param_pid_name";
    public static final String PARAM_RESTAURANT_ID = "param_restaurant_id";
    public static final String PARAM_RESTAURANT_NAME = "param_restaurant_name";
    public static final String PARAM_NUMBER = "param_number";
    public static final String PARAM_ORDER_ID = "param_order_id";
    public static final String PARAM_HOTEL_ID = "param_hotel_id";
    public static final String PARAM_ORDER_IDS = "param_order_ids";
    public static final String PARAM_SHOP_ID = "param_shop_id";
    public static final String PARAM_SHOP_NAME = "param_shop_name";
    public static final String PARAM_SHOP_IMAGE = "param_shop_image";
    public static final String PARAM_DELIVERY_TYPE = "param_delivery_type";
    public static final String PARAM_DELIVERY_FEE = "param_delivery_fee";
    public static final String PARAM_GOOD_ID = "param_good_id";
    public static final String PARAM_GOOD_NUMBER = "param_good_number";
    public static final String PARAM_PRICE = "PARAM_PRICE";
    public static final String PARAM_GOOD_NAME = "param_good_name";
    public static final String PARAM_GOOD_IMAGE = "param_good_image";
    public static final String PARAM_ORDER_COMMIT_DATA = "param_order_commit_data";
    public static final String PARAM_CHILD_CATEGORY_ID = "param_child_category_id";
    public static final String PARAM_CATEGORY_ID = "param_category_id";

    /**
     * 账号
     */
    public static final String PARAM_ACCOUNT = "account";
    public static final String PARAM_COUNT = "count";

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
    public static final String PARAM_NEWS_ID = "newsId";
    public static final String PARAM_DATA = "param_data";
    public static final String PARAM_TOTAL_PRICE = "param_total_price";
    public static final String PARAM_NAME = "param_name";
    public static final String PARAM_BOOLEAN = "param_boolean";
    public static final String PARAM_DESCRIPTION = "param_description";
    public static final String PARAM_SELECT_IMAGE = "param_select_image";

    /**
     * 包装费
     */
    public static final String PARAM_PACKING_CHARGES = "param_packing_charges";

    /**
     * 配送费
     */
    public static final String PARAM_DELIVERY_COST = "param_delivery_cost";


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
     * 支付等待时间
     */
    public static final long WAIT_DELAYS_PAY = 60 * 30;


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
    public static final String SP_IS_FIRST_OPEN = "sp_is_first_open";
    public static final String SP_IS_AGREE_USER_AGREEMENT = "sp_is_agree_user_agreement_20210422";
    public static final String SP_DEVICE_ID = "sp_device_id";
    public static final String SP_LATITUDE = "sp_latitude";
    public static final String SP_LONGITUDE = "sp_longitude";



    public static final String PARAM_ADDRESS_ID = "param_address_id";
    public static final String PARAM_ADDRESS = "param_address";
    public static final String PARAM_RECIPIENT = "param_recipient";
    public static final String PARAM_SEX_MAN = "param_sex_man";
    public static final String PARAM_SEX= "param_sex";
    public static final String PARAM_PHONE = "param_phone";



    public static final int PAY_INFO_ESTORE = 0;
    public static final int PAY_INFO_ESTORE_BILLS = 1;
    public static final int PAY_INFO_FOOD = 2;
    public static final int PAY_INFO_TAKEAWAY = 3;
    public static final int PAY_INFO_HOTEL = 4;



}

