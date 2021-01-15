package com.jcs.where.utils;

import android.util.Log;

import java.util.Locale;

/**
 * create by zyf on 2021/1/6 11:45 上午
 */
public class CacheUtil {

    /**
     * 若不需要更新，返回存储的Json字符串
     * 否则返回 ""
     *
     * @param spKey 存储到SP时使用的key
     * @return Json字符串或""
     */
    public static String needUpdateBySpKey(String spKey) {
        String jsonData = SPUtil.getInstance().getString(spKey);
        long savedTime = 0;
        String jsonStr = "";
        try {
            if (jsonData != null) {
                String[] strArray = jsonData.split(SPKey.K_DELIMITER);
                savedTime = Long.parseLong(strArray[1]);
                jsonStr = strArray[0];
            }
        } catch (Exception e) {
            Log.e("CacheUtil", "needUpdateBySpKey:" + spKey + "-- " + e.getMessage());
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - savedTime <= SPKey.SAVE_TIME) {
            // 获取网路数据
            return jsonStr;
        }
        return "";
    }

    public static void cacheWithCurrentTime(String key, Object value) {
        String jsonStr;
        if (value instanceof String) {
            jsonStr = (String) value;
        } else {
            jsonStr = JsonUtil.getInstance().toJsonStr(value);
        }
        String valueWithTime = jsonStr + SPKey.K_DELIMITER + System.currentTimeMillis();
        SPUtil.getInstance().saveString(key, valueWithTime);
    }

    public static void cacheLanguage(String language) {
        SPUtil.getInstance().saveString(SPKey.K_LANGUAGE, language);
    }

    /**
     * 获取保存的语言环境
     * 默认为 auto
     */
    public static String getLanguageFromCache() {
        String language = SPUtil.getInstance().getString(SPKey.K_LANGUAGE);
        return language.equals("") ? "en" : language;
    }
}
