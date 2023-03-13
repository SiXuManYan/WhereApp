package com.jiechengsheng.city.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/7 3:43 下午
 */
public class SearchResponse {

    /**
     * id : 1044
     * name : Casa Vea Pastries Shop
     * created_at : 2020-11-26 17:52:32
     * type : 3
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("type")
    private Integer type;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
