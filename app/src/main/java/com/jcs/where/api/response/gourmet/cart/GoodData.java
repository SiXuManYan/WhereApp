package com.jcs.where.api.response.gourmet.cart;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wangsw  2021/4/7 11:47.
 */
public class GoodData implements Serializable {

    public int id;
    public String title = "";
    public String image = "";
    public BigDecimal price = BigDecimal.ZERO;
    public BigDecimal original_price =  BigDecimal.ZERO;

}
