package com.jiechengsheng.city.utils;

import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.maps.model.LatLng;
import com.jiechengsheng.city.BuildConfig;

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
    public static String needUpdateBySpKeyByLanguage(String spKey) {
        String jsonData = SPUtil.getInstance().getString(getLanguageFromCache() + SPKey.K_DELIMITER + spKey);
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

    public static void saveToken(String token) {
        SPUtils.getInstance().put(SPKey.K_TOKEN, token);
    }


    public static void cacheWithCurrentTimeByLanguage(String key, Object value) {
        String jsonStr;
        if (value instanceof String) {
            jsonStr = (String) value;
        } else {
            jsonStr = JsonUtil.getInstance().toJsonStr(value);
        }
        String valueWithTime = jsonStr + SPKey.K_DELIMITER + System.currentTimeMillis();
        SPUtil.getInstance().saveString(getLanguageFromCache() + SPKey.K_DELIMITER + key, valueWithTime);
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

    public static String getToken() {
        return SPUtils.getInstance().getString(SPKey.K_TOKEN, "");
    }

    public static SPUtils getShareDefault() {
        return SPUtils.getInstance(Constant.PR_DEFAULT);
    }

    /**
     * 是否授权用户协议
     * @return
     */
    public static boolean isAgreeUserAgreement(){
        return getShareDefault().getBoolean(Constant.SP_IS_AGREE_USER_AGREEMENT, false);
    }

    /**
     * 授权用户协议
     * @param isAgree
     */
    public static void putIsAgreeUserAgreement(boolean isAgree){
        getShareDefault().put(Constant.SP_IS_AGREE_USER_AGREEMENT, isAgree);
    }



    public static LatLng getMyCacheLocation() {

        double lat = 0;
        double lng = 0;
        if (BuildConfig.FLAVOR == "dev") {
            lat = Constant.LAT;
            lng = Constant.LNG;
        } else {
            lat = getShareDefault().getFloat(Constant.SP_MY_LATITUDE, 0);
            lng = getShareDefault().getFloat(Constant.SP_MY_LONGITUDE, 0);
        }
        return new LatLng(lat, lng);
    }

    /**
     * 获取选择城市的经纬度
     *
     * @return
     */
    public static LatLng getSafeSelectLatLng() {

        float selectLat = SPUtils.getInstance().getFloat(SPKey.SELECT_LAT, (float) Constant.LAT);
        float selectLng = SPUtils.getInstance().getFloat(SPKey.SELECT_LNG, (float) Constant.LNG);

        return new LatLng(selectLat, selectLng);

    }


}
