package com.jcs.where.api.request;

public class HotelOrderRequest {

    /**
     * hotel_room_id : 1
     * price : 200
     * username : username
     * phone : 13800000000
     * start_date : 2020-06-17
     * end_date : 2020-06-18
     * room_num : 1
     * country_code : 86
     */

    private String hotel_room_id;
    private String price;
    private String username;
    private String phone;
    private String start_date;
    private String end_date;
    private String room_num;
    private String country_code;

    public String getHotel_room_id() {
        return hotel_room_id;
    }

    public void setHotel_room_id(String hotel_room_id) {
        this.hotel_room_id = hotel_room_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
