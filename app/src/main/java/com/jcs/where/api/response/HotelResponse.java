package com.jcs.where.api.response;

import com.jcs.where.widget.LabelView;

import java.util.List;

public class HotelResponse {

    /**
     * id : 75
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"]
     * name : Wilkinson-Parker Hotel
     * grade : 5
     * comment_counts : 17
     * tags : [{"name":"机场周边","pivot":{"hotel_id":75,"hotel_tag_id":5}},{"name":"地铁周边","pivot":{"hotel_id":75,"hotel_tag_id":4}}]
     * address : 6963 Dibbert Station Suite 000
     * West Alysaview, ID 58620
     * lat : 14.7147249
     * lng : 120.3734724
     * price : 871
     * distance : 0
     * remain_room_num : 0
     * facebook_link : https://www.facebook.com
     */

    private int id;
    private String name;
    private double grade;
    private int comment_counts;
    private String address;
    private double lat;
    private double lng;
    private int price;
    private int distance;
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

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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

    public static class TagsBean implements LabelView.LabelNameInterface {
        /**
         * name : 机场周边
         * pivot : {"hotel_id":75,"hotel_tag_id":5}
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

        @Override
        public String getLabelName() {
            return name;
        }

        public static class PivotBean {
            /**
             * hotel_id : 75
             * hotel_tag_id : 5
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
