package com.jcs.where.api.response.category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/29 10:49.
 * 分类
 */
public class Category {

    public String id;
    /**
     * 分类名称
     */
    public String name = "";

    /**
     * 是否有下级（1：没有，2：有）
     */
    public int has_children;

    /**
     * 分类类型（0：综合服务，1：酒店，2：旅游景点，3：政府，4：旅游行业，5：餐厅，6：企业黄页）
     */
    public int type;

    /**
     * icon图标
     */
    public String icon;

    /**
     * 子集
     */
    public List<Category> child_categories = new ArrayList<>();

    /**
     * 本地字段，判断是否选中
     */
    public boolean nativeIsSelected = false;

    public boolean follow_status ;
    public boolean is_default ;

}


