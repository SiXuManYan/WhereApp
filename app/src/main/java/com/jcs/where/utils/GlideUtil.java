package com.jcs.where.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;

/**
 * create by zyf on 2021/1/14 11:25 下午
 */
public class GlideUtil {
   private static RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.ic_glide_default)//图片加载出来前，显示的图片
            .fallback(R.mipmap.ic_glide_default) //url为空的时候,显示的图片
            .error(R.mipmap.ic_glide_default);

    public static void load(Context context, String iconUrl, ImageView target) {
        Glide.with(context).load(iconUrl).apply(options).into(target);
    }
    public static void loadGif(Context context, String iconUrl, ImageView target) {
        Glide.with(context).asGif().load(iconUrl).apply(options).into(target);
    }
}
