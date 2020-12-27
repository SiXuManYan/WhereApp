package com.jcs.where.api.response;

import java.util.List;

/**
 * create by zyf on 2020/12/27 3:30 PM
 */
public class HotelRoomListResponse {

    /**
     * id : ID
     * images : 图片
     * name : 房间名称
     * hotel_room_type : 酒店类型
     * breakfast_type : 早餐类型（1：有，2：没有）
     * room_area : 房间面积
     * room_num : 房间数量
     * remain_room_num : 剩余房间数量
     * price : 价格
     * is_cancel : 是否可取消（1：可取消，2：不可取消）
     */

    private int id;
    private List<String> images;
    private String name;
    private String hotel_room_type;
    private int breakfast_type;
    private String room_area;
    private String room_num;
    private int remain_room_num;
    private int price;
    private String is_cancel;

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

    public String getHotel_room_type() {
        return hotel_room_type;
    }

    public void setHotel_room_type(String hotel_room_type) {
        this.hotel_room_type = hotel_room_type;
    }

    public int getBreakfast_type() {
        return breakfast_type;
    }

    public void setBreakfast_type(int breakfast_type) {
        this.breakfast_type = breakfast_type;
    }

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public int getRemain_room_num() {
        return remain_room_num;
    }

    public void setRemain_room_num(int remain_room_num) {
        this.remain_room_num = remain_room_num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
