package com.jcs.where.bean;

import java.util.List;

public class GuessYouLikeHotelBean {

    /**
     * id : 1
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"]
     * name : Crown Royale Hotel
     * grade : 1
     * comment_counts : 3
     * tags : [{"name":"测试1","pivot":{"hotel_id":1,"hotel_tag_id":1}},{"name":"测试2","pivot":{"hotel_id":1,"hotel_tag_id":2}}]
     * address : Capitol Dr, City of Balanga, 2100 Bataan
     * lat : 14.676989
     * lng : 120.535781
     * price : 1220
     * distance : 0
     * remain_room_num : 0
     */

    private int id;
    private String name;
    private float grade;
    private int comment_counts;
    private String address;
    private float lat;
    private float lng;
    private float price;
    private float distance;
    private int remain_room_num;
    private String facebook_link;
    private List<String> images;
    private List<TagsBean> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getComment_counts() {
        return comment_counts;
    }

    public void setComment_counts(int comment_counts) {
        this.comment_counts = comment_counts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getRemain_room_num() {
        return remain_room_num;
    }

    public void setRemain_room_num(int remain_room_num) {
        this.remain_room_num = remain_room_num;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * name : 测试1
         * pivot : {"hotel_id":1,"hotel_tag_id":1}
         */

        private String name;
        private PivotBean pivot;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PivotBean getPivot() {
            return pivot;
        }

        public void setPivot(PivotBean pivot) {
            this.pivot = pivot;
        }

        public static class PivotBean {
            /**
             * hotel_id : 1
             * hotel_tag_id : 1
             */

            private int hotel_id;
            private int hotel_tag_id;

            public int getHotel_id() {
                return hotel_id;
            }

            public void setHotel_id(int hotel_id) {
                this.hotel_id = hotel_id;
            }

            public int getHotel_tag_id() {
                return hotel_tag_id;
            }

            public void setHotel_tag_id(int hotel_tag_id) {
                this.hotel_tag_id = hotel_tag_id;
            }
        }
    }
}
