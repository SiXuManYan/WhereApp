package com.jcs.where.bean;

import java.util.List;

public class TravelDetailBean {

    /**
     * id : 1
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/travels/%28%E7%85%A7%E7%89%87%29%E5%8F%B0%E7%81%A3%E5%8D%81%E5%A4%A7%E8%A1%8C%E7%A8%8B-%E6%97%A5%E6%9C%88%E6%BD%AD-S.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/10.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/Cii_JV13DySIASD7AAkMkPopAi0AAIdogPrrFAACQyo594_w640_h320_c1_t0.png","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%283%29.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/travels/unnamed%20%284%29.jpg"]
     * name : Heathcote Ltd
     * address : 5647 Marvin Keys
     * Julienland, HI 47332
     * lat : 14.5854206
     * lng : 120.3406515
     * grade : 3.9
     * comments_count : 36
     * is_collect : 0
     * start_time : 08:00
     * end_time : 18:00
     * content : At this moment the King, who had been anything near the centre of the jurymen. 'It isn't a bird,' Alice remarked. 'Oh, you foolish Alice!' she answered herself. 'How can you learn lessons in the.
     * notice : Alice, and she hurried out of the lefthand bit of the Lizard's slate-pencil, and the jury eagerly wrote down all three to settle the question, and they lived at the bottom of the others all joined.
     */

    private int id;
    private String name;
    private String address;
    private double lat;
    private double lng;
    private double grade;
    private int comments_count;
    private int is_collect;
    private String start_time;
    private String end_time;
    private String content;
    private String notice;
    private String phone;
    private List<String> images;

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

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
