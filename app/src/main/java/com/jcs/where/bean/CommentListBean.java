package com.jcs.where.bean;

import java.util.List;

public class CommentListBean {

    /**
     * data : [{"id":1,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png"],"star":3,"created_at":"2020-06-10 14:22:55","hotel_id":1,"comment_travel_type_id":1,"username":"TestName1","avatar":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","user_id":1,"content":"test content"},{"id":2,"images":[],"star":1,"created_at":"2020-06-17 09:23:54","hotel_id":1,"comment_travel_type_id":2,"username":"TestName11","avatar":null,"user_id":11,"content":"12312321"},{"id":3,"images":[],"star":1,"created_at":"2020-06-17 09:24:52","hotel_id":1,"comment_travel_type_id":3,"username":"TestName11","avatar":null,"user_id":11,"content":"12312321"},{"id":8,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-room-design-compressor.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotelreview1a.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/iStock-185270129.jpg"],"star":3,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":2,"username":"TestName1","avatar":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","user_id":1,"content":"Alice; but she had read about them in books, and."},{"id":9,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/124033028.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/50569-istock-486178472.webp","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/IMG_0012-2.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-room.jpg"],"star":5,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":3,"username":"TestName2","avatar":null,"user_id":2,"content":"What happened to me! When I used to say.' 'So he."},{"id":10,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/131703031.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/50569-istock-486178472.webp","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/BBlf9Zf.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/img_universal.jpg"],"star":2,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":1,"username":"TestName3","avatar":null,"user_id":3,"content":"I to get into the open air. 'IF I don't know,'."},{"id":11,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/JW-Marriot-hotel-room-Galaxy-Macau-Phase-2-e1432637852679.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/b6aa915a8af1139561f0b9ec24a2e5af.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-1979406_1920.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/offers-room-berjaya-times-square-hotel-kuala-lumpur.jpg"],"star":1,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":2,"username":"TestName4","avatar":null,"user_id":4,"content":"The judge, by the prisoner to--to somebody.' 'It."},{"id":12,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/167534297.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-1979406_1920.jpg"],"star":1,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":1,"username":"TestName5","avatar":null,"user_id":5,"content":"Dormouse, not choosing to notice this question."},{"id":13,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/124033028.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-room.jpg"],"star":3,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":3,"username":"TestName6","avatar":null,"user_id":6,"content":"Alice. 'Why, you don't know the song, she kept."},{"id":14,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/Hotelroom-Alamy.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotelroom-2000x1200.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/iStock-185270129.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/img_universal.jpg"],"star":5,"created_at":"2020-07-28 10:52:58","hotel_id":1,"comment_travel_type_id":2,"username":"TestName7","avatar":null,"user_id":7,"content":"Time, and round goes the clock in a melancholy."}]
     * links : {"first":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=1","last":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=2","prev":null,"next":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=2"}
     * meta : {"current_page":1,"from":1,"last_page":2,"path":"https://api.jcstest.com/hotelapi/v1/hotel/1/comments","per_page":10,"to":10,"total":20}
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
         * last : https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=2
         * prev : null
         * next : https://api.jcstest.com/hotelapi/v1/hotel/1/comments?page=2
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
         * path : https://api.jcstest.com/hotelapi/v1/hotel/1/comments
         * per_page : 10
         * to : 10
         * total : 20
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
        public boolean is_select = false;

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
