package com.jcs.where.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HotelMapListBean {

    /**
     * id : 58
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/531665e3a379a2b576.jpg"]
     * name : Ledner, Friesen and Reichert Hotel
     * grade : 3.2
     * comment_counts : 17
     * tags : [{"name":"火车站周边","pivot":{"hotel_id":58,"hotel_tag_id":7}},{"name":"地铁周边","pivot":{"hotel_id":58,"hotel_tag_id":4}}]
     * address : 72744 Rippin Flat Apt. 401
     * Mabelstad, MA 91146-7898
     * lat : 14.6824169
     * lng : 120.557012
     * price : 722
     * distance : 2.882
     * remain_room_num : 0
     * facebook_link : https://www.facebook.com
     */

    private int id;
    private String name;
    private float grade;
    public int comment_counts;
    private String address;
    private double lat;
    private double lng;
    public int price;
    private double distance;
    private int remain_room_num;
    private String facebook_link;
    public ArrayList<String> images = new ArrayList<>();




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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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




    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * name : 火车站周边
         * pivot : {"hotel_id":58,"hotel_tag_id":7}
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
             * hotel_id : 58
             * hotel_tag_id : 7
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
