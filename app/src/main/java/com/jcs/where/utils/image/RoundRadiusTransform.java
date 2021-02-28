package com.jcs.where.utils.image;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Created by Wangsw  2021/2/27 15:39.
 * glide 圆角
 */
public class RoundRadiusTransform extends BitmapTransformation {

    private static final String ID = "com.jcs.where.utils.image.RoundRadiusTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final int roundingRadius ;

    private boolean isLeftTop, isRightTop, isLeftBottom, isRightBottom;


    public RoundRadiusTransform(int roundingRadius) {
        this.roundingRadius = roundingRadius;
    }


    public RoundRadiusTransform(int roundingRadius,boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        this.roundingRadius = roundingRadius;
        setNeedCorner(leftTop,rightTop,leftBottom,rightBottom);
    }

    /**
     * 需要设置圆角的部分
     *
     */
    public void setNeedCorner(boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        isLeftTop = leftTop;
        isRightTop = rightTop;
        isLeftBottom = leftBottom;
        isRightBottom = rightBottom;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.roundedCorners(pool, toTransform,
                isLeftTop ? roundingRadius : 0,
                isRightTop ? roundingRadius : 0,
                isRightBottom ? roundingRadius : 0,
                isLeftBottom ? roundingRadius : 0);
    }



    @Override
    public boolean equals(Object o) {
        if (o instanceof RoundRadiusTransform) {
            RoundRadiusTransform other = (RoundRadiusTransform) o;
            return roundingRadius == other.roundingRadius;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(roundingRadius));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
        messageDigest.update(radiusData);
    }

}
