package com.jcs.where.api.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.jcs.where.news.view_type.NewsType;
import com.jcs.where.search.bean.ISearchResponse;

import java.util.List;

/**
 * create by zyf on 2021/1/18 9:58 下午
 */
public class NewsResponse implements MultiItemEntity, ISearchResponse {

    /**
     * id : 7
     * content_type : 1
     * title : I had our Dinah here, I know all the things being alive; for instance, there's.
     * cover_images : ["https://whereoss.oss-accelerate.aliyuncs.com/news/covers/S5Y6QuO1S8xXUm.jpg"]
     * video_time :
     * video_link :
     * publisher : {"id":11,"nickname":"Where","avatar":""}
     * comment_num : 0
     * read_num : 9725
     * follow_status : 1
     * collect_status : 1
     * created_at : 5个月前
     */

    @SerializedName("id")
    private int id;
    @SerializedName("content_type")
    private Integer contentType;
    @SerializedName("title")
    private String title;
    @SerializedName("video_time")
    private String videoTime;
    @SerializedName("video_link")
    private String videoLink;
    @SerializedName("publisher")
    private PublisherDTO publisher;
    @SerializedName("comment_num")
    private Integer commentNum;
    @SerializedName("read_num")
    private Integer readNum;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("collect_status")
    private Integer collectStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("cover_images")
    private List<String> coverImages;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public PublisherDTO getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherDTO publisher) {
        this.publisher = publisher;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
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

    public List<String> getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(List<String> coverImages) {
        this.coverImages = coverImages;
    }

    @Override
    public int getItemType() {
        switch (contentType) {
            case 1:
                //图文
                int size = coverImages.size();
                if (size > 1) {
                    // 三个图
                    return NewsType.THREE_IMAGE;
                } else if (size < 1) {
                    // 纯文本
                    return NewsType.TEXT;
                } else {
                    // 单图
                    return NewsType.SINGLE_IMAGE;
                }
            case 2:
                // 视频
                return NewsType.VIDEO;
        }
        return 0;
    }

    @Override
    public String getName() {
        return title;
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
