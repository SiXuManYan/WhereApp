package com.jiechengsheng.city.api.response;

import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2020/12/14 9:09 PM
 */
public class ParentCategoryResponse extends BaseNode {


    /**
     * id : 1
     * name : 政府
     * type : 3
     * icon : https://whereoss.oss-accelerate.aliyuncs.com/images/apMYBIPKhBOVTDLbERwUcLiDuuL0wQjiPiQY8TUB.png
     * has_children : 2
     * child_categories : [{"id":2,"name":"国家政府部门","type":3,"icon":"https://whereoss.oss-accelerate.aliyuncs.com/images/wn38gyzADvJlX8yZSgYWhjyOu5dRQbQg6I7zzuMy.png","has_children":1},{"id":3,"name":"省政府部门","type":3,"icon":"https://whereoss.oss-accelerate.aliyuncs.com/images/lbeu2GzzR2PfYyqTOTp3BwMTqnqwxEAB80hzz6cR.png","has_children":2},{"id":4,"name":"市/区政府部门","type":3,"icon":"https://whereoss.oss-accelerate.aliyuncs.com/images/5WqJ14nJkLecgJCbI6uIIN2WeFF2tFGv8W5ICCFm.png","has_children":2},{"id":131,"name":"国有控股公司","type":0,"icon":"https://whereoss.oss-accelerate.aliyuncs.com/images/sZ1oPaAU822AZje3ISljiWXCHmeEbjD1J8lYwRUV.png","has_children":1}]
     */

    private Integer id;
    private String name;
    private Integer type;
    private String icon;
    private Integer has_children;
    private List<CategoryResponse> child_categories;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getHas_children() {
        return has_children;
    }

    public void setHas_children(Integer has_children) {
        this.has_children = has_children;
    }

    public List<CategoryResponse> getChild_categories() {
        return child_categories;
    }

    public void setChild_categories(List<CategoryResponse> child_categories) {
        this.child_categories = child_categories;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        int size = this.child_categories.size();
        for (int i = 0; i < size; i++) {
            this.child_categories.get(i).setParentCategory(this);
        }
        return new ArrayList<>(child_categories);
    }
}
