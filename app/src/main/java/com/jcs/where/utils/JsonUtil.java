package com.jcs.where.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.api.response.CategoryResponse;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json 工具类
 * create by zyf on 2021/1/4 8:59 下午
 */
public class JsonUtil {
    private static JsonUtil mInstance;
    private Gson mGson;

    private JsonUtil() {
        mGson = new Gson();
    }

    //采用Double CheckLock(DCL)实现单例
    public static JsonUtil getInstance() {
        if (mInstance == null) {
            synchronized (LocationUtil.class) {
                if (mInstance == null) {
                    mInstance = new JsonUtil();
                }
            }
        }
        return mInstance;
    }

    public String toJsonStr(Object obj) {
        return mGson.toJson(obj);
    }

    public <T> T fromJson(String jsonStr, Class<T> clazz) {
        return (T) mGson.fromJson(jsonStr, clazz);
    }

    public <T> List<T> fromJsonToList(String jsonStr, Type typeOfT) {
        return mGson.fromJson(jsonStr, typeOfT);
    }
}
