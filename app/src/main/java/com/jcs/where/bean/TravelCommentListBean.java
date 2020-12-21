package com.jcs.where.bean;

import java.util.List;

public class TravelCommentListBean {

    /**
     * data : [{"id":971,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/e00f3cf7891aa3f2851f8483524cd51a326276.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%281%29.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%284%29.jpg"],"star_level":5,"created_at":"2020-07-29 11:16:23","username":"TestName7","avatar":null,"content":"Alice)--'and perhaps you were all ornamented with hearts. Next came the."},{"id":970,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/20190520174759-52.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/750481159d5bdabd8807d79d39fa1699.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/langkawi.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/maxresdefault.jpg"],"star_level":2,"created_at":"2020-07-29 11:16:23","username":"TestName6","avatar":null,"content":"Shall I try the effect: the next moment a shower of saucepans, plates, and."},{"id":969,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/c6609f7bf489408c971e4610c79fb4d4.jpeg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/e00f3cf7891aa3f2851f8483524cd51a326276.jpg"],"star_level":1,"created_at":"2020-07-29 11:16:23","username":"TestName5","avatar":null,"content":"This is the same height as herself; and when she was considering in her face."},{"id":968,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6d830c4ff14e0fb8c4128cb071312a44111017.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/750481159d5bdabd8807d79d39fa1699.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/Cii_JV13DySIASD7AAkMkPopAi0AAIdogPrrFAACQyo594_w640_h320_c1_t0.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%283%29.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed.jpg"],"star_level":4,"created_at":"2020-07-29 11:16:23","username":"TestName4","avatar":null,"content":"However, she soon found herself in a minute or two she walked sadly down the."},{"id":967,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6d830c4ff14e0fb8c4128cb071312a44111017.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/c26m7qgad5ebngi4uzwuaoyy2i9q3urv.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%283%29.jpg"],"star_level":2,"created_at":"2020-07-29 11:16:23","username":"TestName3","avatar":null,"content":"Queen left off, quite out of the month is it?' 'Why,' said the Duchess."},{"id":966,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/6da778ea0625419d836fecebecb483a5_th.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/wk1.1-1024x683.jpg"],"star_level":2,"created_at":"2020-07-29 11:16:23","username":"TestName2","avatar":null,"content":"IT. It's HIM.' 'I don't see any wine,' she remarked. 'It tells the day and."},{"id":965,"images":["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/Cii_JV13DySIASD7AAkMkPopAi0AAIdogPrrFAACQyo594_w640_h320_c1_t0.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/maxresdefault.jpg"],"star_level":5,"created_at":"2020-07-29 11:16:23","username":"TestName1","avatar":"https://whereoss.oss-cn-beijing.aliyuncs.com/images/2rB2zJ1XA9Q1mkId19DbmIhI4MkuCwnQepTeK6PT.png","content":"Alice! when she got back to the shore, and then another confusion of."}]
     * links : {"first":"https://api.jcstest.com/travelapi/v1/comment/57?page=1","last":"https://api.jcstest.com/travelapi/v1/comment/57?page=2","prev":"https://api.jcstest.com/travelapi/v1/comment/57?page=1","next":null}
     * meta : {"current_page":2,"from":11,"last_page":2,"path":"https://api.jcstest.com/travelapi/v1/comment/57","per_page":10,"to":17,"total":17}
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
         * first : https://api.jcstest.com/travelapi/v1/comment/57?page=1
         * last : https://api.jcstest.com/travelapi/v1/comment/57?page=2
         * prev : https://api.jcstest.com/travelapi/v1/comment/57?page=1
         * next : null
         */

        private String first;
        private String last;
        private String prev;
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

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
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
         * current_page : 2
         * from : 11
         * last_page : 2
         * path : https://api.jcstest.com/travelapi/v1/comment/57
         * per_page : 10
         * to : 17
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
        public boolean is_select = false;
        /**
         * id : 971
         * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/e00f3cf7891aa3f2851f8483524cd51a326276.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%281%29.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%284%29.jpg"]
         * star_level : 5
         * created_at : 2020-07-29 11:16:23
         * username : TestName7
         * avatar : null
         * content : Alice)--'and perhaps you were all ornamented with hearts. Next came the.
         */

        private int id;
        private int star_level;
        private String created_at;
        private String username;
        private String avatar;
        private String content;
        private List<String> images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStar_level() {
            return star_level;
        }

        public void setStar_level(int star_level) {
            this.star_level = star_level;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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
