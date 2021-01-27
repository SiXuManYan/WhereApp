package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * create by zyf on 2021/1/26 9:11 下午
 */
public class NewsDetailResponse {

    /**
     * id : 17
     * uuid : f7d11800-59e1-4dd1-847e-8c1f072ebf0c
     * content_type : 1
     * title : After a time she had put the Lizard in head downwards, and the whole pack of.
     * publisher : {"id":11,"nickname":"Where","avatar":""}
     * video_link :
     * video_time :
     * article_link : https://api.jcstest.com/news/17
     * read_num : 9880
     * follow_status : 1
     * collect_status : 1
     * created_at : 5个月前
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("content_type")
    private Integer contentType;
    @SerializedName("title")
    private String title;
    @SerializedName("publisher")
    private PublisherDTO publisher;
    @SerializedName("video_link")
    private String videoLink;
    @SerializedName("video_time")
    private String videoTime;
    @SerializedName("article_link")
    private String articleLink;
    @SerializedName("read_num")
    private Integer readNum;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("collect_status")
    private Integer collectStatus;
    @SerializedName("created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PublisherDTO getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherDTO publisher) {
        this.publisher = publisher;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public Integer getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(Integer collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class PublisherDTO {
        /**
         * id : 11
         * nickname : Where
         * avatar :
         */

        @SerializedName("id")
        private Integer id;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("avatar")
        private String avatar;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
