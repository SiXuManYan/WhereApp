package com.jiechengsheng.city.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP 工具类
 * SharedPreference 工具类
 * create by zyf on 2020/12/29 9:07 PM
 */
public class SPUtil {
    private static SPUtil mInstance;
    private SharedPreferences mSharedPreferences;

    private SPUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences("where", Context.MODE_PRIVATE);
    }

    //采用Double CheckLock(DCL)实现单例
    public static SPUtil initInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocationUtil.class) {
                if (mInstance == null) {
                    mInstance = new SPUtil(context);
                }
            }
        }
        return mInstance;
    }

    public static SPUtil getInstance() {
        return mInstance;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }
}
