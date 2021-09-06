package com.jcs.where.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * create by zyf on 2021/1/2 11:42 AM
 */
public class MechanismDetailResponse implements Serializable {

    /**
     * id : 1
     * uuid : 55b1bfe1-5841-49fb-99ee-9c877577f3c5
     * title : Aldyen's Catering Service
     * images : ["https://whereoss.oss-accelerate.aliyuncs.com/shops/webp%20%282%29.webp","https://whereoss.oss-accelerate.aliyuncs.com/shops/webp%20%283%29.webp","https://whereoss.oss-accelerate.aliyuncs.com/shops/webp%20%2821%29.webp"]
     * week_start : 1
     * week_end : 5
     * start_time : 09:00
     * end_time : 21:00
     * address : Joshua St Bataan Homes San Jose Balanga
     * lat : 14.6704114
     * lng : 120.534082
     * tel : 0999-9970331
     * abstract : 
     * collect_status : 2
     * extension_tel : 
     * web_site : 
     * email : 
     * facebook : 
     */

    public int id;
    public String uuid;
    public String title;
    public int week_start;
    public int week_end;
    public String start_time;
    public String end_time;
    public String address;
    public Double lat;
    public Double lng;
    public String tel;
    @SerializedName("abstract")
    public String abstractX;
    public int collect_status;
    public String extension_tel;
    public String web_site;
    public String email;
    public String facebook;
    public List<String> images = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeek_start() {
        return week_start;
    }

    public void setWeek_start(int week_start) {
        this.week_start = week_start;
    }

    public int getWeek_end() {
        return week_end;
    }

    public void setWeek_end(int week_end) {
        this.week_end = week_end;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public int getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(int collect_status) {
        this.collect_status = collect_status;
    }

    public String getExtension_tel() {
        return extension_tel;
    }

    public void setExtension_tel(String extension_tel) {
        this.extension_tel = extension_tel;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
