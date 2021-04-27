package com.jcs.where.api.response.gourmet.cart;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Wangsw  2021/4/7 11:42.
 */
public class Products implements Serializable {

    public int cart_id;
    public int good_num;
    public GoodData good_data = new GoodData();
    public boolean nativeIsSelect = false;



}
