package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;
import com.jcs.where.utils.MapMarkerUtil;

import java.util.List;

/**
 * create by zyf on 2021/1/28 10:08 下午
 */
public class TouristAttractionResponse implements MapMarkerUtil.IMapData{

    /**
     * id : 101
     * image : ["https://whereoss.oss-accelerate.aliyuncs.com/images/AXN8j8v7zdwxYG5e7LitWz9Yz2CpfIDDAjG2RQyd.jpeg"]
     * name : Las Casas Filipinas De Acuzar
     * address : Bagac - Mariveles Rd, Mariveles, Bataan, Philippines
     * lat : 14.4569984
     * lng : 120.4674525
     * grade : 3.5
     * tags : [{"name":"标签1","pivot":{"scenic_id":101,"tag_id":1}},{"name":"标签2","pivot":{"scenic_id":101,"tag_id":2}},{"name":"标签3","pivot":{"scenic_id":101,"tag_id":3}},{"name":"标签4","pivot":{"scenic_id":101,"tag_id":4}},{"name":"标签5","pivot":{"scenic_id":101,"tag_id":5}},{"name":"标签6","pivot":{"scenic_id":101,"tag_id":6}}]
     * comments_count : 2
     * distance : 23.3
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;
    @SerializedName("grade")
    private Double grade;
    @SerializedName("comments_count")
    private Integer commentsCount;
    @SerializedName("distance")
    private Double distance;
    @SerializedName("image")
    private List<String> image;
    @SerializedName("tags")
    private List<TagsDTO> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getImages() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<TagsDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagsDTO> tags) {
        this.tags = tags;
    }

    public static class TagsDTO {
        /**
         * name : 标签1
         * pivot : {"scenic_id":101,"tag_id":1}
         */

        @SerializedName("name")
        private String name;
        @SerializedName("pivot")
        private PivotDTO pivot;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PivotDTO getPivot() {
            return pivot;
        }

        public void setPivot(PivotDTO pivot) {
            this.pivot = pivot;
        }

        public static class PivotDTO {
            /**
             * scenic_id : 101
             * tag_id : 1
             */

            @SerializedName("scenic_id")
            private Integer scenicId;
            @SerializedName("tag_id")
            private Integer tagId;

            public Integer getScenicId() {
                return scenicId;
            }

            public void setScenicId(Integer scenicId) {
                this.scenicId = scenicId;
            }

            public Integer getTagId() {
                return tagId;
            }

            public void setTagId(Integer tagId) {
                this.tagId = tagId;
            }
        }
    }

    @Override
    public String toString() {
        return "TouristAttractionResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", grade=" + grade +
                ", commentsCount=" + commentsCount +
                ", distance=" + distance +
                ", image=" + image +
                ", tags=" + tags +
                '}';
    }
}
