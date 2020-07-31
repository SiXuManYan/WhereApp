package com.jcs.where.bean;

import java.util.List;

public class HotelListBean {


    /**
     * data : [{"id":40,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Roob, Nienow and O'Reilly Hotel","grade":3.9,"comment_counts":17,"tags":[{"name":"商务出行","pivot":{"hotel_id":40,"hotel_tag_id":1}},{"name":"火车站周边","pivot":{"hotel_id":40,"hotel_tag_id":7}},{"name":"休闲度假","pivot":{"hotel_id":40,"hotel_tag_id":2}}],"address":"172 Moen Ramp\nSouth Dayne, WY 88212-6609","lat":14.6135404,"lng":120.3235242,"price":652,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":19,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Labadie-Daniel Hotel","grade":3.7,"comment_counts":17,"tags":[{"name":"商务出行","pivot":{"hotel_id":19,"hotel_tag_id":1}},{"name":"海滨风光","pivot":{"hotel_id":19,"hotel_tag_id":6}}],"address":"369 Kiehn Isle\nWest Albinmouth, TX 25500","lat":14.5555306,"lng":120.36784,"price":732,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":4,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Hammes Ltd Hotel","grade":3.6,"comment_counts":17,"tags":[{"name":"儿童乐园","pivot":{"hotel_id":4,"hotel_tag_id":3}},{"name":"地铁周边","pivot":{"hotel_id":4,"hotel_tag_id":4}}],"address":"918 Jermaine Manor Apt. 089\nNew Martine, ME 54113-0989","lat":14.6107452,"lng":120.5379946,"price":583,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":79,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"name":"Hudson, Morar and Mraz Hotel","grade":3.6,"comment_counts":17,"tags":[{"name":"休闲度假","pivot":{"hotel_id":79,"hotel_tag_id":2}},{"name":"机场周边","pivot":{"hotel_id":79,"hotel_tag_id":5}},{"name":"儿童乐园","pivot":{"hotel_id":79,"hotel_tag_id":3}}],"address":"986 Huels Islands\nDickensfort, TN 99284","lat":14.6934139,"lng":120.4567078,"price":93,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":32,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/3e4ba663f8a6c48698079b42b3a1926c.jpg"],"name":"Yundt, D'Amore and Williamson Hotel","grade":3.5,"comment_counts":17,"tags":[{"name":"儿童乐园","pivot":{"hotel_id":32,"hotel_tag_id":3}},{"name":"休闲度假","pivot":{"hotel_id":32,"hotel_tag_id":2}}],"address":"84890 Peggie Place Suite 335\nLake Aidenmouth, ME 22579-3314","lat":14.7971529,"lng":120.4952361,"price":533,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":81,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/531665e3a379a2b576.jpg"],"name":"Vandervort-Altenwerth Hotel","grade":3.5,"comment_counts":17,"tags":[{"name":"机场周边","pivot":{"hotel_id":81,"hotel_tag_id":5}},{"name":"商务出行","pivot":{"hotel_id":81,"hotel_tag_id":1}}],"address":"243 Towne Bridge Apt. 887\nWest Olaf, WY 12883-5676","lat":14.5255816,"lng":120.5434874,"price":922,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":33,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Ledner-Jaskolski Hotel","grade":3.4,"comment_counts":17,"tags":[{"name":"儿童乐园","pivot":{"hotel_id":33,"hotel_tag_id":3}},{"name":"海滨风光","pivot":{"hotel_id":33,"hotel_tag_id":6}}],"address":"100 Koby Lakes Apt. 762\nLake Junior, SC 96155-5512","lat":14.5290211,"lng":120.3777488,"price":537,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":85,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/16256-118839-f64451367_3xl.webp"],"name":"Bauch, Gaylord and Okuneva Hotel","grade":3.4,"comment_counts":17,"tags":[{"name":"商务出行","pivot":{"hotel_id":85,"hotel_tag_id":1}},{"name":"火车站周边","pivot":{"hotel_id":85,"hotel_tag_id":7}}],"address":"144 Stokes Mountain Apt. 024\nMalcolmbury, PA 88083-5628","lat":14.7761945,"lng":120.5681218,"price":969,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":93,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Klein Ltd Hotel","grade":3.4,"comment_counts":17,"tags":[{"name":"火车站周边","pivot":{"hotel_id":93,"hotel_tag_id":7}},{"name":"儿童乐园","pivot":{"hotel_id":93,"hotel_tag_id":3}}],"address":"2563 Ankunding Spurs\nNorth Karine, AR 39526-2104","lat":14.5098376,"lng":120.3542191,"price":1023,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"},{"id":11,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"],"name":"Corwin-Bradtke Hotel","grade":3.3,"comment_counts":17,"tags":[{"name":"火车站周边","pivot":{"hotel_id":11,"hotel_tag_id":7}},{"name":"儿童乐园","pivot":{"hotel_id":11,"hotel_tag_id":3}}],"address":"57236 Glover Station Apt. 877\nMarvinbury, MI 79044-9927","lat":14.8471217,"lng":120.4417259,"price":1751,"distance":0,"remain_room_num":0,"facebook_link":"https://www.facebook.com"}]
     * links : {"first":"https://api.jcstest.com/hotelapi/v1/hotels?page=1","last":"https://api.jcstest.com/hotelapi/v1/hotels?page=5","prev":null,"next":"https://api.jcstest.com/hotelapi/v1/hotels?page=2"}
     * meta : {"current_page":1,"from":1,"last_page":5,"path":"https://api.jcstest.com/hotelapi/v1/hotels","per_page":10,"to":10,"total":42}
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
         * last : https://api.jcstest.com/hotelapi/v1/hotels?page=5
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
         * last_page : 5
         * path : https://api.jcstest.com/hotelapi/v1/hotels
         * per_page : 10
         * to : 10
         * total : 42
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
         * id : 40
         * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"]
         * name : Roob, Nienow and O'Reilly Hotel
         * grade : 3.9
         * comment_counts : 17
         * tags : [{"name":"商务出行","pivot":{"hotel_id":40,"hotel_tag_id":1}},{"name":"火车站周边","pivot":{"hotel_id":40,"hotel_tag_id":7}},{"name":"休闲度假","pivot":{"hotel_id":40,"hotel_tag_id":2}}]
         * address : 172 Moen Ramp
         South Dayne, WY 88212-6609
         * lat : 14.6135404
         * lng : 120.3235242
         * price : 652
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

        public static class TagsBean {
            /**
             * name : 商务出行
             * pivot : {"hotel_id":40,"hotel_tag_id":1}
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
                 * hotel_id : 40
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
