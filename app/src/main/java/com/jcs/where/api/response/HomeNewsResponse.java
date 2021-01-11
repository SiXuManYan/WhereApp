package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/11 11:43 上午
 */
public class HomeNewsResponse {

    /**
     * id : 100
     * title : Gryphon, and all the rest of it now in sight, and no room to grow here,' said.
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
