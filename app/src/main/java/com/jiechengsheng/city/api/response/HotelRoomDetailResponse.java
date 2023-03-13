package com.jiechengsheng.city.api.response;

import java.util.List;

/**
 * 酒店房间详情
 * create by zyf on 2020/12/27 3:46 PM
 */
public class HotelRoomDetailResponse {

    /**
     * id : 1
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/AC-Hotel-Park-City.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/BBlf9Zf.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/JW-Marriot-hotel-room-Galaxy-Macau-Phase-2-e1432637852679.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/Muji-Hotel-Shenzhen-02-hotel-room-design-trends-italianbark-.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotel-room.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/phk-deluxe-harbour-view-suite-living-room-1074.jpg"]
     * name : family suite
     * hotel_room_type : 多床
     * room_area : 11-20
     * window_type : 1
     * floor : 5-8
     * wifi_type : 1
     * people : 2
     * policy : Parking: A fee-paying parking lot
     * shower_room : Electric water heater, separate shower, hair dryer, separate bathroom, telephone, slippers, 24 hours hot water
     * facility : Various specifications power outlets, wardrobe/wardrobe, sewing kit, high speed Internet access in room, dark curtain, automatic curtain, desk, WIFI free in guest room, air conditioning
     * media : Domestic long distance telephone, limited channel, LCD TV
     * food : Electric kettle, coffee pot/teapot, bottled water free, mini bar, mini fridge
     * scene : outdoor landscape
     * other :
     * room_num : 11
     * remain_room_num : null
     * price : 500
     * is_cancel : 1
     * cancel_time : 18:00
     */

    private Integer id;
    private String name;
    private String hotel_room_type;
    private String room_area;
    private Integer window_type;
    private String floor;
    private Integer wifi_type;
    private Integer people;
    private String policy;
    private String shower_room;
    private String facility;
    private String media;
    private String food;
    private String scene;
    private String other;
    private Integer room_num;
    private Object remain_room_num;
    private Integer price;
    private Integer is_cancel;
    private String cancel_time;
    private List<String> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public Integer getWindow_type() {
        return window_type;
    }

    public void setWindow_type(Integer window_type) {
        this.window_type = window_type;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getWifi_type() {
        return wifi_type;
    }

    public void setWifi_type(Integer wifi_type) {
        this.wifi_type = wifi_type;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getShower_room() {
        return shower_room;
    }

    public void setShower_room(String shower_room) {
        this.shower_room = shower_room;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Integer getRoom_num() {
        return room_num;
    }

    public void setRoom_num(Integer room_num) {
        this.room_num = room_num;
    }

    public Object getRemain_room_num() {
        return remain_room_num;
    }

    public void setRemain_room_num(Object remain_room_num) {
        this.remain_room_num = remain_room_num;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(Integer is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
