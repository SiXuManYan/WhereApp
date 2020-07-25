package com.jcs.where.bean;

import java.util.List;

public class HotelCommentBean {

    /**
     * data : [{"id":1,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"star":3,"created_at":"2020-06-10 14:22:55","hotel_id":1,"comment_travel_type_id":1,"username":"TestName1","avatar":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","user_id":1,"content":"test content"},{"id":2,"images":[],"star":1,"created_at":"2020-06-17 09:23:54","hotel_id":1,"comment_travel_type_id":2,"username":"TestName11","avatar":null,"user_id":11,"content":"12312321"},{"id":3,"images":[],"star":1,"created_at":"2020-06-17 09:24:52","hotel_id":1,"comment_travel_type_id":3,"username":"TestName11","avatar":null,"user_id":11,"content":"12312321"}]
     * links : {"first":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=1","last":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=1","prev":null,"next":null}
     * meta : {"current_page":1,"from":1,"last_page":1,"path":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments","per_page":10,"to":3,"total":3}
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
         * first : https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=1
         * last : https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=1
         * prev : null
         * next : null
         */

        private String first;
        private String last;
        private Object prev;
        private Object next;

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

        public Object getNext() {
            return next;
        }

        public void setNext(Object next) {
            this.next = next;
        }
    }

    public static class MetaBean {
        /**
         * current_page : 1
         * from : 1
         * last_page : 1
         * path : https://api.jcstest.com/hotelapi/v1/hotel/1/comments
         * per_page : 10
         * to : 3
         * total : 3
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
         * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"]
         * star : 3
         * created_at : 2020-06-10 14:22:55
         * hotel_id : 1
         * comment_travel_type_id : 1
         * username : TestName1
         * avatar : https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png
         * user_id : 1
         * content : test content
         */

        private int id;
        private int star;
        private String created_at;
        private int hotel_id;
        private int comment_travel_type_id;
        private String username;
        private String avatar;
        private int user_id;
        private String content;
        private List<String> images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getHotel_id() {
            return hotel_id;
        }

        public void setHotel_id(int hotel_id) {
            this.hotel_id = hotel_id;
        }

        public int getComment_travel_type_id() {
            return comment_travel_type_id;
        }

        public void setComment_travel_type_id(int comment_travel_type_id) {
            this.comment_travel_type_id = comment_travel_type_id;
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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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
}
