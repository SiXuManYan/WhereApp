package com.jcs.where;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.comm100.livechat.VisitorClientInterface;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.storage.WhereDataBase;
import com.jcs.where.utils.CrashHandler;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.SPUtil;

import java.util.Locale;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application {

    public WhereDataBase getDatabase() {
        return database;
    }

    private WhereDataBase database = null;

    public int count = 0;

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        RetrofitManager.getManager().initRetrofit(this);
        LocationUtil.initInstance(this);
        SPUtil.initInstance(this);
        changeLanguage();
//        setLanguage();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initComm100();
        database = WhereDataBase.get(this);
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

    /**
     * 初始化 comm 100 客服
     */
    private void initComm100() {
//        VisitorClientInterface.setChatUrl("https://chatserver.comm100.com/chatWindow.aspx?planId=260&siteId=223747");
        VisitorClientInterface.setChatUrl("https://vue.livelyhelp.chat/chatWindow.aspx?siteId=60001617&planId=8da6d622-e5b2-4741-bcdc-855d5d82f95a#");

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        BasicPushNotificationBuilder builder =
                new BasicPushNotificationBuilder(this.getApplicationContext());

        builder.statusBarDrawable = R.drawable.icon_small;
        builder.notificationFlags = Notification.FLAG_SHOW_LIGHTS;
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;

        JPushInterface.setDefaultPushNotificationBuilder(builder);

        if (Build.VERSION.SDK_INT > 23) {
            JPushInterface.setLatestNotificationNumber(this.getApplicationContext(), 1);
        }

        String regId = JPushInterface.getRegistrationID(this);
        if (!regId.equals("")) {
            VisitorClientInterface.setRemoteNotificationDeviceId(regId);
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                Log.v("BaseApplication", activity + "onActivityStopped");
                count--;
                if (count == 0) {
                    VisitorClientInterface.activeRemoteNotification();
                    Log.v("BaseApplication", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.v("BaseApplication", activity + "onActivityStarted");
                if (count == 0) {
                    VisitorClientInterface.inactiveRemoteNotification();
                    Log.v("BaseApplication", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.v("BaseApplication", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.v("BaseApplication", activity + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.v("BaseApplication", activity + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.v("BaseApplication", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.v("BaseApplication", activity + "onActivityCreated");
            }
        });

    }

    public static void toLogin() {
        Intent toLogin = new Intent(mApplicationContext, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplicationContext.startActivity(toLogin);
    }

}
