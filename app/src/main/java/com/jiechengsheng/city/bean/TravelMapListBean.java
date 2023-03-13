package com.jiechengsheng.city.bean;

import java.util.List;

public class TravelMapListBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 63
         * image : https://whereoss.oss-cn-beijing.aliyuncs.com/travels/5760f7907a120.jpeg
         * name : Jacobs-Bashirian
         * address : 3073 Donato Roads Apt. 421
         * Daisyfort, OH 80601
         * lat : 14.6811019
         * lng : 120.5206797
         * grade : 3.2
         * tags : ["标签3","标签2","标签1"]
         * comments_count : 17
         * km : 1
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
