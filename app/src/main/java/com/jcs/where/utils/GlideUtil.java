package com.jcs.where.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;

import java.io.File;
import java.text.DecimalFormat;

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

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }


    private static final double GB = 1024L * 1024L * 1024L;
    private static final double MB = 1024L * 1024L;
    private static final double KB = 1024L;

    public static String byteConversionGBMBKB(double kSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        double temp = 0;
        if (kSize / GB >= 1) {
            temp = kSize / GB;
            return df.format(temp) + "GB";
        } else if (kSize / MB >= 1) {
            temp = kSize / MB;
            return df.format(temp) + "MB";
        } else if (kSize / KB >= 1) {
            temp = kSize / KB;
            return df.format(temp) + "KB";
        }
        return kSize + "B";
    }


    /**
     * 清理图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context){
        try{
            if(Looper.myLooper() == Looper.getMainLooper()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            }else{
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(final Context context){
        try{
            if(Looper.myLooper() == Looper.getMainLooper()){
                Glide.get(context).clearMemory();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
