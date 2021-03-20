package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * create by zyf on 2021/1/18 9:25 下午
 */
public class NewsChannelResponse extends BaseNode implements Serializable {

    /**
     * id : 4
     * name : 美食
     * followStatus ：是否关注（1：已关注）（2：未关注）
     */

    @SerializedName("id")
    private int id = 0;
    @SerializedName("name")
    private String name = "";
    @SerializedName("follow_status")
    private int followStatus ;

    /**
     * 从网络拉取的都是可编辑的
     * 只有本地添加的：（关注）（推荐）是不可编辑的，需要手动设置
     */
    private boolean isEditable = true;

    public NewsChannelResponse() {
    }

    public NewsChannelResponse(String name) {
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

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
