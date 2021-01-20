package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * create by zyf on 2021/1/20 10:42 下午
 */
public class ParentNewsTabResponse extends BaseNode {
    /**
     * 我的频道
     * 更多频道
     */
    private String prompt;

    /**
     * 点击进入频道（listener）
     * 点击添加频道（提示）
     */
    private String action;

    private boolean isShowEdit;

    private List<BaseNode> children;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isShowEdit() {
        return isShowEdit;
    }

    public void setShowEdit(boolean showEdit) {
        isShowEdit = showEdit;
    }

    public List< BaseNode> getChildren() {
        return children;
    }

    public void setChildren(List<BaseNode> children) {
        this.children = children;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return getChildren();
    }
}
