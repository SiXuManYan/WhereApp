package com.jiechengsheng.city.api.response;

import java.util.List;

public class HotelOrderDetailResponse {

    /**
     * id : 1
     * price : 200
     * trade_no : 18202006171517274426
     * order_status : 5
     * hotel_id : 0
     * hotel_name : McDermott-Balistreri Hotel
     * hotel_addr : 25632 Cali Unions Apt. 909
     * South Toy, NV 81128-1696
     * hotel_tel : 13130033471
     * hotel_lat : 14.6171339
     * hotel_lng : 120.3683139
     * images : ["https://whereoss.oss-accelerate.aliyuncs.com/hotels/1246280_16061017110043391702.jpg"]
     * grade : 2
     * room_name : family suite
     * room_num : 1
     * room_type : 多床
     * is_cancel : 1
     * cancel_time : 16:35
     * breakfast_type : 2
     * wifi_type : 1
     * window_type : 1
     * start_date : 6月17日(星期三)
     * end_date : 6月18日(星期四)
     * username : ["username"]
     * phone : 13800000000
     * people_num : 2
     * days : 1
     * created_at : 2020-06-17 15:17:27
     */

    private int id;
    private int price;
    private String trade_no;
    private int order_status;
    private int hotel_id;
    private String hotel_name;
    private String hotel_addr;
    private String hotel_tel;
    private double hotel_lat;
    private double hotel_lng;
    private int grade;
    private String room_name;
    private int room_num;
    private String room_type;
    private int is_cancel;
    private String cancel_time;
    private int breakfast_type;
    private int wifi_type;
    private int window_type;
    private String start_date;
    private String end_date;
    private String username;
    private String phone;
    private int people_num;
    private int days;
    private String created_at;
    private List<String> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
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

    public int getWindow_type() {
        return window_type;
    }

    public void setWindow_type(int window_type) {
        this.window_type = window_type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


}
