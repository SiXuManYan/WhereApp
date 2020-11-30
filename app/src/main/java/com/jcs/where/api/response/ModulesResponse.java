package com.jcs.where.api.response;

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
    private List<Integer> categories;

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

    public int getDev_status() {
        return dev_status;
    }

    public void setDev_status(int dev_status) {
        this.dev_status = dev_status;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }
}
