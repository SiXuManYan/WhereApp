package com.jcs.where.api.response;

import java.util.List;

public class HotelDetailResponse {

    /**
     * id : 1
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"]
     * name : Crown Royale Hotel
     * start_business_time : 2010-01-01
     * address : Capitol Dr, City of Balanga, 2100 Bataan
     * lat : 14.676989
     * lng : 120.535781
     * tel : +63472371961
     * grade : 1
     * comment_counts : 3
     * facilities : [{"name":"名字1","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":1}},{"name":"名字2","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":2}},{"name":"名字3","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":3}},{"name":"名字4","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":4}},{"name":"名字5","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":5}},{"name":"名字6","icon":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/G6l2jEFmujhKcVlXHzqff82HXN9Oi93dqhJyjhVw.png","pivot":{"hotel_id":1,"hotel_facility_id":6}}]
     * policy : {"children":"可以","check_in_time":"14:00:00","check_out_time":"12:00::00"}
     * collect_status : 2
     * facebook_link : https://facebook.com
     */

    private int id;
    private String name;
    private String start_business_time;
    private String address;
    private double lat;
    private double lng;
    private String tel;
    private double grade;
    private int comment_counts;
    private PolicyBean policy;
    private int collect_status;
    private String facebook_link;
    private List<String> images;
    private List<FacilitiesBean> facilities;

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

    public PolicyBean getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyBean policy) {
        this.policy = policy;
    }

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

        private String children;
        private String check_in_time;
        private String check_out_time;
        private String pet;
        private String hint;
        private String payment;
        private String breadfast;
        private String service_desc;

        public String getChildren() {
            return children;
        }

        public void setChildren(String children) {
            this.children = children;
        }

        public String getCheck_in_time() {
            return check_in_time;
        }

        public void setCheck_in_time(String check_in_time) {
            this.check_in_time = check_in_time;
        }

        public String getCheck_out_time() {
            return check_out_time;
        }

        public void setCheck_out_time(String check_out_time) {
            this.check_out_time = check_out_time;
        }

        public String getPet() {
            return pet;
        }

        public void setPet(String pet) {
            this.pet = pet;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public String getBreadfast() {
            return breadfast;
        }

        public void setBreadfast(String breadfast) {
            this.breadfast = breadfast;
        }

        public String getService_desc() {
            return service_desc;
        }

        public void setService_desc(String service_desc) {
            this.service_desc = service_desc;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }
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
