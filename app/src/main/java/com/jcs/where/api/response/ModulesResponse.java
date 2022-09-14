package com.jcs.where.api.response;

import java.util.ArrayList;
import java.util.List;

public class ModulesResponse {
    /**
     * id : 1
     * name : 政府机构
     * icon : https://whereoss.oss-cn-beijing.aliyuncs.com/jgq_icon/%E6%94%BF%E5%BA%9C%E6%9C%BA%E6%9E%84.png
     * categories : [1]
     * dev_status : 1
     */

    private int id;
    private String name;
    private String icon;
    private int dev_status;
    private ArrayList<Integer> categories = new ArrayList<>();

    /**
     * 1：政府机构->带地图的综合服务页面
     * 2：企业黄页->三级联动筛选的综合服务页面
     * 3：旅游住宿->旅游住宿二级页
     * 4：便民服务、教育机构、健康&医疗、家政服务->横向二级联动筛选的综合服务页面
     * 5：金融服务->横向二级联动筛选的综合服务页面（注：分类需获取到Finance分类下的三级分类）
     * 6：餐饮美食->餐厅列表
     * 7：线上商店->Comming soon
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 1：已上线 2：开发中
     *
     * @return
     */
    public int getDev_status() {
        return dev_status;
    }

    public void setDev_status(int dev_status) {
        this.dev_status = dev_status;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Integer> categories) {
        this.categories = categories;
    }
}
