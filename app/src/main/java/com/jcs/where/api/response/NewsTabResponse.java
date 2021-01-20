package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * create by zyf on 2021/1/18 9:25 下午
 */
public class NewsTabResponse extends BaseNode implements Serializable {

    /**
     * id : 4
     * name : 美食
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;

    public NewsTabResponse() {
    }

    public NewsTabResponse(String name) {
        this.name = name;
    }

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

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
