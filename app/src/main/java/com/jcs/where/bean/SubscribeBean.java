package com.jcs.where.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SubscribeBean implements Parcelable {
    public int roomId;
    public String roomName;
    public String bed;
    public String breakfast;
    public int window;
    public int wifi;
    public int people;
    public int cancel;
    public String hotelName;
    public String startYear;
    public String startDate;
    public String startWeek;
    public String endYear;
    public String endDate;
    public String endWeek;
    public String night;
    public String roomNumber;
    public float roomPrice;

    public SubscribeBean(Parcel in) {
        roomId = in.readInt();
        roomName = in.readString();
        bed = in.readString();
        breakfast = in.readString();
        window = in.readInt();
        wifi = in.readInt();
        people = in.readInt();
        cancel = in.readInt();
        hotelName = in.readString();
        startDate = in.readString();
        startWeek = in.readString();
        endDate = in.readString();
        endWeek = in.readString();
        night = in.readString();
        roomNumber = in.readString();
        roomPrice = in.readFloat();
        startYear = in.readString();
        endYear = in.readString();
    }

    public SubscribeBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roomId);
        dest.writeString(roomName);
        dest.writeString(bed);
        dest.writeString(breakfast);
        dest.writeInt(window);
        dest.writeInt(wifi);
        dest.writeInt(people);
        dest.writeInt(cancel);
        dest.writeString(hotelName);
        dest.writeString(startDate);
        dest.writeString(startWeek);
        dest.writeString(endDate);
        dest.writeString(endWeek);
        dest.writeString(night);
        dest.writeString(roomNumber);
        dest.writeFloat(roomPrice);
        dest.writeString(startYear);
        dest.writeString(endYear);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscribeBean> CREATOR = new Creator<SubscribeBean>() {
        @Override
        public SubscribeBean createFromParcel(Parcel in) {
            return new SubscribeBean(in);
        }

        @Override
        public SubscribeBean[] newArray(int size) {
            return new SubscribeBean[size];
        }
    };
}
