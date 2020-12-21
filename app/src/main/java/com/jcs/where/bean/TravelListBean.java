package com.jcs.where.bean;

import java.util.List;

public class TravelListBean {

    /**
     * data : [{"id":57,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6d830c4ff14e0fb8c4128cb071312a44111017.jpg","name":"McDermott-Corkery","address":"50717 Maeve Creek\nDexterfort, KS 76682-2742","lat":14.596036,"lng":120.49796,"grade":3.1,"tags":["标签6","标签4"],"comments_count":17,"km":2},{"id":28,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/20190520174759-52.jpg","name":"Lakin Group","address":"37512 Pamela Locks Apt. 020\nEast Niko, WI 92878","lat":14.5826462,"lng":120.462684,"grade":2.8,"tags":["标签5","标签3","标签1"],"comments_count":17,"km":2},{"id":49,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/10.png","name":"McClure Ltd","address":"595 Federico Isle\nLake Graciela, NM 59469","lat":14.5788936,"lng":120.4587901,"grade":3,"tags":["标签5","标签6","标签1"],"comments_count":17,"km":3},{"id":83,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","name":"Rosenbaum, Johnson and Conroy","address":"809 Marie Rue\nNorth Gladyceport, SD 74627-1641","lat":14.6199288,"lng":120.4807707,"grade":3.6,"tags":["标签3","标签4"],"comments_count":17,"km":3},{"id":93,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/750481159d5bdabd8807d79d39fa1699.jpg","name":"Kulas, Lueilwitz and Conn","address":"109 Kaci Forges\nLake Baileeland, GA 58702-1042","lat":14.5954412,"lng":120.5102914,"grade":3.1,"tags":["标签2","标签1"],"comments_count":17,"km":3},{"id":69,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","name":"Kirlin-Watsica","address":"96532 Lucius Land\nLake Tracy, ME 35091-7051","lat":14.5559463,"lng":120.4722088,"grade":2.9,"tags":["标签5","标签2"],"comments_count":17,"km":4},{"id":80,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/Cii_JV13DySIASD7AAkMkPopAi0AAIdogPrrFAACQyo594_w640_h320_c1_t0.png","name":"Bogan PLC","address":"77112 Wisoky Crossing Suite 718\nNellaton, WI 48916","lat":14.6278165,"lng":120.5008561,"grade":3,"tags":["标签1","标签4","标签2"],"comments_count":17,"km":5},{"id":58,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6d830c4ff14e0fb8c4128cb071312a44111017.jpg","name":"Cummings Inc","address":"646 Hudson Wells Apt. 491\nSpinkachester, MD 59685","lat":14.5880243,"lng":120.4387407,"grade":3.5,"tags":["标签6","标签5"],"comments_count":17,"km":5},{"id":82,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6da778ea0625419d836fecebecb483a5_th.jpg","name":"Rice, Volkman and Anderson","address":"619 Cleta Curve Apt. 541\nEltonstad, PA 46988-9952","lat":14.5663827,"lng":120.4307036,"grade":2.6,"tags":["标签4","标签3"],"comments_count":17,"km":6},{"id":11,"image":"https://whereoss.oss-cn-beijing.aliyuncs.com/travels/20190520174759-52.jpg","name":"Bogan-Kub","address":"279 Jovan Club\nEast Rossieside, DC 28822","lat":14.5641459,"lng":120.5325098,"grade":2.6,"tags":["标签6","标签3"],"comments_count":17,"km":6}]
     * links : {"first":"https://api.jcstest.com/travelapi/v1/travel?page=1","last":"https://api.jcstest.com/travelapi/v1/travel?page=10","prev":null,"next":"https://api.jcstest.com/travelapi/v1/travel?page=2"}
     * meta : {"current_page":1,"from":1,"last_page":10,"path":"https://api.jcstest.com/travelapi/v1/travel","per_page":10,"to":10,"total":100}
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
         * first : https://api.jcstest.com/travelapi/v1/travel?page=1
         * last : https://api.jcstest.com/travelapi/v1/travel?page=10
         * prev : null
         * next : https://api.jcstest.com/travelapi/v1/travel?page=2
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
         * last_page : 10
         * path : https://api.jcstest.com/travelapi/v1/travel
         * per_page : 10
         * to : 10
         * total : 100
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
         * id : 57
         * image : https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6d830c4ff14e0fb8c4128cb071312a44111017.jpg
         * name : McDermott-Corkery
         * address : 50717 Maeve Creek
         * Dexterfort, KS 76682-2742
         * lat : 14.596036
         * lng : 120.49796
         * grade : 3.1
         * tags : ["标签6","标签4"]
         * comments_count : 17
         * km : 2
         */

        private int id;
        private String image;
        private String name;
        private String address;
        private double lat;
        private double lng;
        private double grade;
        private int comments_count;
        private int km;
        private List<String> tags;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
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

        public double getGrade() {
            return grade;
        }

        public void setGrade(double grade) {
            this.grade = grade;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public int getKm() {
            return km;
        }

        public void setKm(int km) {
            this.km = km;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
