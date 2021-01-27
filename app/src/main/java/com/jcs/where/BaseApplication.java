package com.jcs.where;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.blankj.utilcode.util.LanguageUtils;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.CrashHandler;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.SPUtil;

import java.util.Locale;

import androidx.multidex.MultiDex;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.getManager().initRetrofit(this);
        LocationUtil.initInstance(this);
        SPUtil.initInstance(this);
        changeLanguage();
//        setLanguage();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public void changeLanguage() {
        Log.e("BaseApplication", "changeLanguage: " + "-----");
        LocalLanguageUtil.getInstance().setApplicationLanguage(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        SPUtil.initInstance(base);
        super.attachBaseContext(LocalLanguageUtil.getInstance().setLocal(base));
        MultiDex.install(this);
    }

    public void setLanguage() {
        // //获得res资源对象
        Resources resources = getResources();

        //获得设置对象
        Configuration configuration = resources.getConfiguration();

        //获取系统当前的语言
        String language = configuration.locale.getLanguage();

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Log.e("BaseApplication", "setLanguage: " + language);
        //根据系统语言进行设置
        if (language.equals("zh")) {
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(configuration, displayMetrics);
//            CacheUtil.cacheLanguage("zh");
        } else if (language.equals("en")) {
            configuration.locale = Locale.US;
            resources.updateConfiguration(configuration, displayMetrics);
//            CacheUtil.cacheLanguage("en");
        }
    }
}
