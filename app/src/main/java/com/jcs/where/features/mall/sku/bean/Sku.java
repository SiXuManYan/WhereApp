package com.jcs.where.features.mall.sku.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.jcs.where.utils.BusinessUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhenzhizao on 2017/3/6.
 */
public class Sku implements Parcelable {
    public String id;
    public String goods_id;
    public String mainImage;
    public int stockQuantity;
    public boolean inStock;
    public BigDecimal originPrice = BigDecimal.ZERO;
    public BigDecimal sellingPrice= BigDecimal.ZERO;
    public List<SkuAttribute> attributes = new ArrayList<>();

    @Override
    public String toString() {
        return "Sku{" +
                "id='" + id + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", inStock=" + inStock +
                ", originPrice=" + originPrice +
                ", sellingPrice=" + sellingPrice +
                ", attributes=" + attributes +
                '}';
    }

    public Sku() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.goods_id);
        dest.writeString(this.mainImage);
        dest.writeInt(this.stockQuantity);
        dest.writeByte(this.inStock ? (byte) 1 : (byte) 0);
        dest.writeString(BusinessUtils.INSTANCE.getSafeBigDecimalString(this.originPrice));
        dest.writeString(BusinessUtils.INSTANCE.getSafeBigDecimalString(this.sellingPrice));
        dest.writeTypedList(this.attributes);
    }

    protected Sku(Parcel in) {
        this.id = in.readString();
        this.goods_id = in.readString();
        this.mainImage = in.readString();
        this.stockQuantity = in.readInt();
        this.inStock = in.readByte() != 0;
        this.originPrice = BusinessUtils.INSTANCE.getSafeBigDecimal(in.readString());
        this.sellingPrice = BusinessUtils.INSTANCE.getSafeBigDecimal(in.readString());
        this.attributes = in.createTypedArrayList(SkuAttribute.CREATOR);
    }

    public static final Creator<Sku> CREATOR = new Creator<Sku>() {
        @Override
        public Sku createFromParcel(Parcel source) {
            return new Sku(source);
        }

        @Override
        public Sku[] newArray(int size) {
            return new Sku[size];
        }
    };
}
