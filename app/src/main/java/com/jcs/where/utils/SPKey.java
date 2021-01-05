package com.jcs.where.utils;

/**
 * create by zyf on 2020/12/29 9:17 PM
 */
public final class SPKey {

    /**
     * 默认存储分类数据一天
     */
    public static final long SAVE_TIME = 60 * 1000 * 60 * 24;

    public static final String K_DELIMITER = "_____";

    /**
     * 存储所有的城市
     */
    public static final String K_ALL_CITIES = "k_all_cities";

    /**
     * 存储当前城市id
     */
    public static final String K_CURRENT_AREA_ID = "k_current_area_id";

    /**
     * 企业黄页页面的分类存储key
     */
    public static final String K_YELLOW_PAGE_CATEGORIES = "k_yellow_page_categories";

    /**
     * 便民服务页面的分类存储key
     */
    public static final String K_CONVENIENCE_SERVICE_CATEGORIES = "k_convenience_service_categories";
}
