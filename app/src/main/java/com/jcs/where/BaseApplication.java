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
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.storage.WhereDataBase;
import com.jcs.where.utils.CrashHandler;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.SPUtil;

import java.util.Locale;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

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
        database = WhereDataBase.get(this);
        RetrofitManager.getManager().initRetrofit(this);
        LocationUtil.initInstance(this);
        SPUtil.initInstance(this);
        changeLanguage();
//        setLanguage();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initComm100();
        initRongCloud();
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

        VisitorClientInterface.setChatUrl(Html5Url.COMM100_CHAT_URL);

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
                count--;
                if (count == 0) {
                    VisitorClientInterface.activeRemoteNotification();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    VisitorClientInterface.inactiveRemoteNotification();
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });

    }

    public static void toLogin() {
        Intent toLogin = new Intent(mApplicationContext, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplicationContext.startActivity(toLogin);
    }


    /**
     * 初始化融云
     */
    private void initRongCloud() {
        RongIM.init(this, BuildConfig.RONG_CLOUD_APP_KEY);
    }

}
