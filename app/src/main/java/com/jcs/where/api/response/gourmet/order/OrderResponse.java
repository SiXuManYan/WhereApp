package com.jcs.where.api.response.gourmet.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Wangsw  2021/4/23 13:39.
 */
public class OrderResponse implements Parcelable {

    /**
     * 订单id
     */
    public int id  = 0;
    /**
     * 订单号
     */
    public String trade_no = "";

    protected OrderResponse(Parcel in) {
        id = in.readInt();
        trade_no = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(trade_no);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderResponse> CREATOR = new Creator<OrderResponse>() {
        @Override
        public OrderResponse createFromParcel(Parcel in) {
            return new OrderResponse(in);
        }

        @Override
        public OrderResponse[] newArray(int size) {
            return new OrderResponse[size];
        }
    };
}
