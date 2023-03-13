package com.jiechengsheng.city.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RoomDetailBean implements Parcelable {

    public static final Creator<RoomDetailBean> CREATOR = new Creator<RoomDetailBean>() {
        @Override
        public RoomDetailBean createFromParcel(Parcel in) {
            return new RoomDetailBean(in);
        }

        @Override
        public RoomDetailBean[] newArray(int size) {
            return new RoomDetailBean[size];
        }
    };
    /**
     * id : 168
     * images : ["https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/124033028.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/AC-Hotel-Park-City.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/Hotelroom-Alamy.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotelreview1a.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/hotelroom-2000x1200.jpg","https://whereoss.oss-cn-beijing.aliyuncs.com/hotels/rooms/iStock-185270129.jpg"]
     * name : double room
     * hotel_room_type : 双床
     * room_area : 12-30
     * window_type : 2
     * floor : 4-8
     * wifi_type : 1
     * people : 2
     * policy : Parking: A fee-paying parking lot
     * shower_room : Electric water heater, separate shower, hair dryer, separate bathroom, telephone, slippers, 24 hours hot water
     * facility : Various specifications power outlets, wardrobe/wardrobe, sewing kit, high speed Internet access in room, dark curtain, automatic curtain, desk, WIFI free in guest room, air conditioning
     * media : Domestic long distance telephone, limited channel, LCD TV
     * food : Electric kettle, coffee pot/teapot, bottled water free, mini bar, mini fridge
     * scene : outdoor landscape
     * other :
     * room_num : 10
     * remain_room_num : null
     * price : 124
     * is_cancel : 1
     */

    private int id;
    private String name;
    private String hotel_room_type;
    private String room_area;
    private int window_type;
    private String floor;
    private int wifi_type;
    private int people;
    private String policy;
    private String shower_room;
    private String facility;
    private String media;
    private String food;
    private String scene;
    private String other;
    private int room_num;
    private Object remain_room_num;
    private int price;
    private int is_cancel;
    private List<String> images;

    protected RoomDetailBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        hotel_room_type = in.readString();
        room_area = in.readString();
        window_type = in.readInt();
        floor = in.readString();
        wifi_type = in.readInt();
        people = in.readInt();
        policy = in.readString();
        shower_room = in.readString();
        facility = in.readString();
        media = in.readString();
        food = in.readString();
        scene = in.readString();
        other = in.readString();
        room_num = in.readInt();
        price = in.readInt();
        is_cancel = in.readInt();
        images = in.createStringArrayList();
    }

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

    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public int getWindow_type() {
        return window_type;
    }

    public void setWindow_type(int window_type) {
        this.window_type = window_type;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getWifi_type() {
        return wifi_type;
    }

    public void setWifi_type(int wifi_type) {
        this.wifi_type = wifi_type;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
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

    public int getRoom_num() {
        return room_num;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public Object getRemain_room_num() {
        return remain_room_num;
    }

    public void setRemain_room_num(Object remain_room_num) {
        this.remain_room_num = remain_room_num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(hotel_room_type);
        parcel.writeString(room_area);
        parcel.writeInt(window_type);
        parcel.writeString(floor);
        parcel.writeInt(wifi_type);
        parcel.writeInt(people);
        parcel.writeString(policy);
        parcel.writeString(shower_room);
        parcel.writeString(facility);
        parcel.writeString(media);
        parcel.writeString(food);
        parcel.writeString(scene);
        parcel.writeString(other);
        parcel.writeInt(room_num);
        parcel.writeInt(price);
        parcel.writeInt(is_cancel);
        parcel.writeStringList(images);
    }
}
