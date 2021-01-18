package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * create by zyf on 2021/1/18 9:25 下午
 */
public class NewsTabResponse implements Serializable {

    /**
     * id : 4
     * name : 美食
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;

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
}
