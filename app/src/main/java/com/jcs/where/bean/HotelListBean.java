package com.jcs.where.bean;

import java.util.List;

public class HotelListBean {

    /**
     * data : [{"id":4,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"THE GRAND PENINSULA SUITES","grade":4,"comment_counts":0,"tags":[],"address":"Duhat St, City of Balanga, Bataan","lat":14.6865551,"lng":120.5459053,"price":897,"distance":0,"remain_room_num":0},{"id":3,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"D' Samat Hotel","grade":3,"comment_counts":0,"tags":[],"address":"32 Camacho St, City of Balanga, Bataan","lat":14.6792776,"lng":120.5423622,"price":2077,"distance":0,"remain_room_num":0},{"id":1,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":3,"tags":[{"name":"测试1","pivot":{"hotel_id":1,"hotel_tag_id":1}},{"name":"测试2","pivot":{"hotel_id":1,"hotel_tag_id":2}}],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":7,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":8,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":9,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":10,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":11,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":12,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0},{"id":13,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"name":"Crown Royale Hotel","grade":1,"comment_counts":0,"tags":[],"address":"Capitol Dr, City of Balanga, 2100 Bataan","lat":14.676989,"lng":120.535781,"price":1220,"distance":0,"remain_room_num":0}]
     * links : {"first":"https://api.jcstest.com/hotelapi/v1/hotels?page=1","last":"https://api.jcstest.com/hotelapi/v1/hotels?page=2","prev":null,"next":"https://api.jcstest.com/hotelapi/v1/hotels?page=2"}
     * meta : {"current_page":1,"from":1,"last_page":2,"path":"https://api.jcstest.com/hotelapi/v1/hotels","per_page":10,"to":10,"total":17}
     */

    private LinksBean links;
    private MetaBean meta;
    private List<DataBean> data;

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class LinksBean {
        /**
         * first : https://api.jcstest.com/hotelapi/v1/hotels?page=1
         * last : https://api.jcstest.com/hotelapi/v1/hotels?page=2
         * prev : null
         * next : https://api.jcstest.com/hotelapi/v1/hotels?page=2
         */

        private String first;
        private String last;
        private Object prev;
        private String next;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public Object getPrev() {
            return prev;
        }

        public void setPrev(Object prev) {
            this.prev = prev;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }

    public static class MetaBean {
        /**
         * current_page : 1
         * from : 1
         * last_page : 2
         * path : https://api.jcstest.com/hotelapi/v1/hotels
         * per_page : 10
         * to : 10
         * total : 17
         */

        private int current_page;
        private int from;
        private int last_page;
        private String path;
        private int per_page;
        private int to;
        private int total;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class DataBean {

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
        private int grade;
        private int comment_counts;
        private String address;
        private double lat;
        private double lng;
        private int price;
        private int distance;
        private int remain_room_num;
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

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
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
}
