package com.jcs.where.api.response.gourmet.comment;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/4/1 17:22.
 * 评论列表
 */
public class CommentResponse {


/*    {
        "id": "ID",
            "images": "图片",
            "content": "内容",
            "star": "星级",
            "created_at": "创建日期",
            "user_id": "用户ID",
            "username": "用户名称",
            "avatar": "用户头像"
    }*/

    public String id = "";
    public ArrayList<String> images = new ArrayList<>();
    public String content = "";
    public String created_at = "";
    public String user_id = "";
    public String username = "";
    public String avatar = "";
    public float star ;



}
