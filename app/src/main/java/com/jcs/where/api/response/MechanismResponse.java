package com.jcs.where.api.response;

import java.util.List;

/**
 * 机构信息
 * create by zyf on 2020/12/28 9:17 PM
 */
public class MechanismResponse {

    /**
     * id : 1
     * images : ["图片"]
     * title : 标题
     * address : 地址
     * lat : 纬度
     * lng : 经度
     * distance : 距离
     * tags : ["标签"]
     */

    private int id;
    private String title;
    private String address;
    private String lat;
    private String lng;
    private String distance;
    private List<String> images;
    private List<String> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
