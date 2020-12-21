package com.jcs.where.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HotelPayBean implements Parcelable {
    public static final Creator<HotelPayBean> CREATOR = new Creator<HotelPayBean>() {
        @Override
        public HotelPayBean createFromParcel(Parcel in) {
            return new HotelPayBean(in);
        }

        @Override
        public HotelPayBean[] newArray(int size) {
            return new HotelPayBean[size];
        }
    };
    public String hotelName;
    public String roomName;
    public String bed;
    public String breakfast;
    public int window;
    public int people;
    public String startDate;
    public String endDate;
    public String night;
    public String roomNumber;
    public String price;
    public String orderId;
    public String trade_no;


    public HotelPayBean() {

    }

    protected HotelPayBean(Parcel in) {
        hotelName = in.readString();
        roomName = in.readString();
        bed = in.readString();
        breakfast = in.readString();
        window = in.readInt();
        people = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        night = in.readString();
        roomNumber = in.readString();
        price = in.readString();
        orderId = in.readString();
        trade_no = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hotelName);
        parcel.writeString(roomName);
        parcel.writeString(bed);
        parcel.writeString(breakfast);
        parcel.writeInt(window);
        parcel.writeInt(people);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(night);
        parcel.writeString(roomNumber);
        parcel.writeString(price);
        parcel.writeString(orderId);
        parcel.writeString(trade_no);
    }
}
