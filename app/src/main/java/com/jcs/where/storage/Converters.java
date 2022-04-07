package com.jcs.where.storage;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.jcs.where.storage.entity.UserRongyunData;

import kotlin.jvm.JvmStatic;

/**
 * Created by Wangsw  2021/2/3 10:51.
 */
public class Converters {


    @TypeConverter
    @JvmStatic
    public static String beanToText(UserRongyunData data) {
        return new Gson().toJson(data);
    }

    @TypeConverter
    @JvmStatic
    public static UserRongyunData textToBean(String value) {
        return new Gson().fromJson(value, UserRongyunData.class);
    }


}
