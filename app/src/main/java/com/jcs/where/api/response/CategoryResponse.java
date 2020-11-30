package com.jcs.where.api.response;

public class CategoryResponse {

    /**
     * id : 119
     * name : 居家服务
     * has_children : 2
     * type : 0
     * icon : https://whereoss.oss-cn-beijing.aliyuncs.com/images/igxYypaTnQuhy3U15KHBGF7SV7Sul2br6vhDoD7y.png
     */

    private int id;
    private String name;
    private int has_children;
    private int type;
    private String icon;

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

    public int getHas_children() {
        return has_children;
    }

    public void setHas_children(int has_children) {
        this.has_children = has_children;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
