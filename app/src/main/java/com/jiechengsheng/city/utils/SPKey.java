package com.jiechengsheng.city.utils;

/**
 * create by zyf on 2020/12/29 9:17 PM
 */
public final class SPKey {

    /**
     * 默认存储分类数据一天
     */
    public static final long SAVE_TIME = 60 * 1000 * 60 * 24;

    public static final String K_DELIMITER = "_____";

    public static final String K_TOKEN = "token";

    /**
     * 存储用户选择的语言
     */
    public static final String K_LANGUAGE = "language";

    /**
     * 存储所有的城市
     */
    public static final String K_ALL_CITIES = "k_all_cities";

    /**
     * 存储当前城市id
     */
    public static final String SELECT_AREA_ID = "k_current_area_id";
    public static final String SELECT_AREA_NAME = "k_current_area_name";
    public static final String SELECT_LAT = "k_current_area_lat";
    public static final String SELECT_LNG = "k_current_area_lng";

    /**
     * 企业黄页页面的分类存储key
     */
    public static final String K_YELLOW_PAGE_CATEGORIES = "k_yellow_page_categories";

     /**
     * 存储企业黄页页面的一级分类的id的key
     */
    public static final String K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID = "k_yellow_page_first_level_category_id";

    /**
     * 综合服务页面的分类存储key，使用时需要在后面拼接分类idid
     */
    public static final String K_SERVICE_CATEGORIES = "k_service_categories_";

    public static final String K_SEARCH_HISTORY = "k_search_history";
    public static final String K_LANGUAGE_TAB = "k_language_tab";

    /**
     * 邀请链接
     */
    public static final String K_INVITE_LINK = "invite_link";

    /**
     * 从剪切板获取到的邀请码(注册成功后清除)
     */
    public static final String K_INVITE_CODE = "invite_code";


}
