package com.jcs.where.utils;

import android.content.Context;
import android.content.SharedPreferences;

import co.tton.android.base.utils.AppUtils;

public final class PreferencesUtils {
    private static final String FILE_NAME = "preferences";

    public static final String KEY_USER = "user";

    public static final String KEY_TOKEN = "toekn";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    // data不能穿null, 否则无法判断是什么类型的数据
    public static void setData(Context context, String key, Object data) {
        assert data != null;

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (data instanceof Integer) {
            editor.putInt(key, (int) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (long) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (float) data);
        } else if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (boolean) data);
        }
        editor.commit();
    }

    // defValue不能穿null, 否则无法判断是什么类型的数据
    public static Object getData(Context context, String key, Object defValue) {
        assert defValue != null;

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defValue instanceof Integer) {
            return preferences.getInt(key, (int) defValue);
        } else if (defValue instanceof Long) {
            return preferences.getLong(key, (long) defValue);
        } else if (defValue instanceof Float) {
            return preferences.getFloat(key, (float) defValue);
        } else if (defValue instanceof String) {
            return preferences.getString(key, (String) defValue);
        } else if (defValue instanceof Boolean) {
            return preferences.getBoolean(key, (boolean) defValue);
        }
        return null;
    }

    public static void removeData(Context context, String key) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean isFirstLaunch(Context context) {
        String version = AppUtils.getVersionName(context);
        String firstLaunch = (String) getData(context, KEY_FIRST_LAUNCH, "");
        return version != null && !version.equals(firstLaunch);
    }

    public static void setFirstLaunch(Context context) {
        setData(context, KEY_FIRST_LAUNCH, AppUtils.getVersionName(context));
    }
}
