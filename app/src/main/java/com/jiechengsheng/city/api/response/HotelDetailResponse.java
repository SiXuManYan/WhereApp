package com.jiechengsheng.city.api.response;

import java.util.List;

public class HotelDetailResponse {



    private int id;
    private String name;
    private String start_business_time;
    private String address;
    private double lat;
    private double lng;
    public String tel;
    private double grade;
    private int comment_counts;
    public PolicyBean policy;
    private int collect_status;

    /**
     * IM聊天开启状态（1：开启，2：关闭
     */
    public int im_status;
    private String facebook_link;


    public String desc;
    private List<String> images;
    private List<FacilitiesBean> facilities;

    /**
     * 视频链接
     */
    public String video = "";

    /**
     * 视频地址
     */
    public String video_image = "";

    /**
     * 酒店星级
     */
    public float star_level ;


    public String uuid = "";
    public String mer_name = "";

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

    public String getStart_business_time() {
        return start_business_time;
    }

    public void setStart_business_time(String start_business_time) {
        this.start_business_time = start_business_time;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

//    public PolicyBean getPolicy() {
//        return policy;
//    }

//    public void setPolicy(PolicyBean policy) {
//        this.policy = policy;
//    }

    public int getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(int collect_status) {
        this.collect_status = collect_status;
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

    public List<FacilitiesBean> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilitiesBean> facilities) {
        this.facilities = facilities;
    }

    public static class PolicyBean {
        /**
         * children : 可以
         * check_in_time : 14:00:00
         * check_out_time : 12:00::00
         */

        public String children = "";
        public String check_in_time = "";
        public String check_out_time = "";
        public String pet;
        public String hint;
        public String payment;
        public String breadfast;
        public String service_desc;

    }

    public static class FacilitiesBean {
        /**
         * name : 名字1
         * icon : https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png
         * pivot : {"hotel_id":1,"hotel_facility_id":1}
         */

        private String name;
        private String icon;
        private PivotBean pivot;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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
             * hotel_facility_id : 1
             */

            private int hotel_id;
            private int hotel_facility_id;

            public int getHotel_id() {
                return hotel_id;
            }

            public void setHotel_id(int hotel_id) {
                this.hotel_id = hotel_id;
            }

            public int getHotel_facility_id() {
                return hotel_facility_id;
            }

            public void setHotel_facility_id(int hotel_facility_id) {
                this.hotel_facility_id = hotel_facility_id;
            }
        }
    }
}
