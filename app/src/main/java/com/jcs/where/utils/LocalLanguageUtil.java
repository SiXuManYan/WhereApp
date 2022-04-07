package com.jcs.where.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * create by zyf on 2021/1/10 7:15 下午
 */
public class LocalLanguageUtil {
    private static LocalLanguageUtil mInstance;

    private LocalLanguageUtil() {
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocalLanguageUtil getInstance() {
        if (mInstance == null) {
            synchronized (LocationUtil.class) {
                if (mInstance == null) {
                    mInstance = new LocalLanguageUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存选择的语言
     */
    public void saveSelectedLanguage(String language) {
        CacheUtil.cacheLanguage(language);
    }

    /**
     * 获取选择的语言设置
     */
    public Locale getSetLanguageLocale(Context context) {
        if (context == null) {
            return Locale.ENGLISH;
        }

        String language = CacheUtil.getLanguageFromCache();

        if (language.equals("zh")) {
            return Locale.CHINA;
        }

        if (language.equals("en")) {
            return Locale.ENGLISH;
        }

        if (language.equals("auto")) {
            return getSystemLocal(context);
        }
        return Locale.CHINA;
    }

    /**
     * 获取系统的locale
     */
    public Locale getSystemLocal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return LocaleList.getDefault().get(0);
        } else {
            return Locale.getDefault();
        }
    }

    public Context setLocal(Context context) {
        return updateResources(context, getSetLanguageLocale(context));
    }

    public Context updateResources(Context context, Locale locale) {
        try {

            Locale.setDefault(locale);
            Resources resources = context.getResources();
            Configuration configuration = new Configuration(resources.getConfiguration());
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        }catch (Exception e){
            e.printStackTrace();
        }
        return context;
    }

    /**
     * 是否是汉语
     */
    public boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

    public boolean isZhEnv(Context context) {
        String languageFromCache = CacheUtil.getLanguageFromCache();
        if (TextUtils.equals(languageFromCache, "auto")) {
            return isZh(context);
        } else {
            return TextUtils.equals(languageFromCache, "zh");
        }
    }

    /**
     * 语言有变化后,全局设置语言类型
     */
    public void setApplicationLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale local = getSetLanguageLocale(context);
        configuration.locale = local;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(local);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(configuration);
            Locale.setDefault(local);
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }

}