package com.jcs.where;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.comm100.livechat.VisitorClientInterface;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.api.response.message.RongCloudUserResponse;
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
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class BaseApplication extends Application {


    private WhereDataBase database = null;

    public WhereDataBase getDatabase() {
        return database;
    }

    public int count = 0;

    private static Context mApplicationContext;

    private BaseApplicationModel mModel;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationContext = this;
        database = WhereDataBase.get(this);
        RetrofitManager.getManager().initRetrofit(this);
        mModel = new BaseApplicationModel();
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
        LocalLanguageUtil.getInstance().setApplicationLanguage(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        SPUtil.initInstance(base);
        super.attachBaseContext(LocalLanguageUtil.getInstance().setLocal(base));
        MultiDex.install(this);
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

        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {

            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */

            @Override
            public boolean onReceived(Message message, int left, boolean hasPackage, boolean offline) {
                return false;
            }
        });


        RongIM.setConnectionStatusListener(connectionStatus -> {

            if (connectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
                setUserInfoProvider();
            }
        });

    }

    /**
     * 设置融云用户信息
     */
    private void setUserInfoProvider() {

        RongIM.setUserInfoProvider(userId -> {

            mModel.getRongCloudUserInfo(new BaseObserver<RongCloudUserResponse>() {

                @Override
                protected void onError(ErrorResponse errorResponse) {

                }

                @Override
                protected void onSuccess(RongCloudUserResponse response) {
                    UserInfo userInfo = new UserInfo(userId, response.name, Uri.parse(response.avatar));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }, userId);

            return null;
        }, true);
    }

}
