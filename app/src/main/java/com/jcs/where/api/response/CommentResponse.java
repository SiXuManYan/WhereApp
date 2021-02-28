package com.jcs.where.api.response;

import android.text.Layout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentResponse {
    public boolean is_select = false;
    public Boolean needFullText = null;

    /**
     * id : 20
     * images : ["https://whereoss.oss-accelerate.aliyuncs.com/travels/6da778ea0625419d836fecebecb483a5_th.jpg","https://whereoss.oss-accelerate.aliyuncs.com/travels/Cii_JV13DySIASD7AAkMkPopAi0AAIdogPrrFAACQyo594_w640_h320_c1_t0.png","https://whereoss.oss-accelerate.aliyuncs.com/travels/unnamed%20%284%29.jpg"]
     * star_level : 4
     * created_at : 2020-08-21 14:21:38
     * username : user_wz
     * avatar : https://whereoss.oss-cn-beijing.aliyuncs.com/images/ahA3yB5Qd6Vrfv3wCqbO8ZipeaIhM7shzQxjBNxy.jpeg
     * content : Five! Don't go splashing paint over me like that!' He got behind Alice as he.
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("star")
    private Integer star;
    @SerializedName("star_level")
    private Integer starLevel;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private List<String> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStarLevel() {
        if (starLevel == null){
            starLevel = star;
        }
        return starLevel;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
