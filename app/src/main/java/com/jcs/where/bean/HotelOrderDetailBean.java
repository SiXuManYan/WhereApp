package com.jcs.where.bean;

import java.util.List;

public class HotelOrderDetailBean {


    /**
     * id : 1
     * price : 200
     * hotel_name : Purdy-Wehner Hotel
     * hotel_addr : 7297 Justina Heights
     Port Gracielachester, NY 94939
     * hotel_tel : (537) 831-0369
     * hotel_lat : 14.492753
     * hotel_lng : 120.5058181
     * room_name : business suite
     * breakfast_type : 2
     * wifi_type : 1
     * start_date : 6月17日(星期三)
     * end_date : 6月18日(星期四)
     * room_num : 1
     * username : ["username"]
     * phone : 13800000000
     * trade_no : 18202006171517274426
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/192958857.jpg"]
     * grade : 3.0
     * room_type : 多床
     * window_type : 1
     * people_num : 3
     * days : 1
     */

    private int id;
    private double price;
    private String hotel_name;
    private String hotel_addr;
    private String hotel_tel;
    private double hotel_lat;
    private double hotel_lng;
    private String room_name;
    private int breakfast_type;
    private int wifi_type;
    private String start_date;
    private String end_date;
    private int room_num;
    private String phone;
    private String trade_no;
    private String grade;
    private String room_type;
    private int window_type;
    private int people_num;
    private int days;
    private List<String> username;
    private List<String> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_addr() {
        return hotel_addr;
    }

    public void setHotel_addr(String hotel_addr) {
        this.hotel_addr = hotel_addr;
    }

    public String getHotel_tel() {
        return hotel_tel;
    }

    public void setHotel_tel(String hotel_tel) {
        this.hotel_tel = hotel_tel;
    }

    public double getHotel_lat() {
        return hotel_lat;
    }

    public void setHotel_lat(double hotel_lat) {
        this.hotel_lat = hotel_lat;
    }

    public double getHotel_lng() {
        return hotel_lng;
    }

    public void setHotel_lng(double hotel_lng) {
        this.hotel_lng = hotel_lng;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getBreakfast_type() {
        return breakfast_type;
    }

    public void setBreakfast_type(int breakfast_type) {
        this.breakfast_type = breakfast_type;
    }

    public int getWifi_type() {
        return wifi_type;
    }

    public void setWifi_type(int wifi_type) {
        this.wifi_type = wifi_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getWindow_type() {
        return window_type;
    }

    public void setWindow_type(int window_type) {
        this.window_type = window_type;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
