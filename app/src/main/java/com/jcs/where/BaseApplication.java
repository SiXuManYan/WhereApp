package com.jcs.where;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.comm100.livechat.VisitorClientInterface;
import com.facebook.FacebookSdk;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.features.message.MessageCenterActivity;
import com.jcs.where.features.message.cloud.ConversationActivity;
import com.jcs.where.features.message.custom.CustomMessage;
import com.jcs.where.features.message.custom.CustomMessageProvider;
import com.jcs.where.frames.common.Html5Url;
import com.jcs.where.storage.WhereDataBase;
import com.jcs.where.storage.entity.User;
import com.jcs.where.storage.entity.UserRongyunData;
import com.jcs.where.utils.BusinessUtils;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.CrashHandler;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.utils.LocationUtil;
import com.jcs.where.utils.SPUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.umcrash.UMCrash;

import java.util.ArrayList;

import cn.jiguang.api.JCoreManager;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.config.RongConfigCenter;
import io.rong.imkit.userinfo.RongUserInfoManager;
import io.rong.imkit.utils.RouteUtils;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
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
        FacebookSdk.sdkInitialize(this);
        mApplicationContext = this;
        database = WhereDataBase.get(this);
        RetrofitManager.getManager().initRetrofit(this);
        mModel = new BaseApplicationModel();
        LocationUtil.initInstance(this);
        SPUtil.initInstance(this);
        changeLanguage();

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        initComm100();
        handleRongCloud();
        handleUmeng();
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


        // 极光推送
        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true);
        }


        // 用户同意协议后的真正注册
        boolean isAgree = CacheUtil.isAgreeUserAgreement();
        if (isAgree) {
            JPushInterface.init(this);
        }

        BasicPushNotificationBuilder builder =
                new BasicPushNotificationBuilder(this.getApplicationContext());

        builder.statusBarDrawable = R.drawable.icon_small;
        builder.notificationFlags = Notification.FLAG_SHOW_LIGHTS;
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;

        JPushInterface.setDefaultPushNotificationBuilder(builder);

        if (Build.VERSION.SDK_INT > 23) {
            // 设置保留最近通知条数 API
            JPushInterface.setLatestNotificationNumber(this.getApplicationContext(), 1);
        }

        if (BuildConfig.DEBUG) {
            JPushInterface.getConnectionState(this);
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
     * 融云相关
     * 详见：https://doc.rongcloud.cn/im/Android/5.X/ui/quick_integration
     */
    private void handleRongCloud() {
        // 初始化
        initRongCloud();

        // 设置融云用户信息提供者
        setUserInfoProvider();

        // 注册自定义消息(在连接前)
        ArrayList<Class<? extends MessageContent>> myMessages = new ArrayList<>();
        myMessages.add(CustomMessage.class);
        RongIMClient.registerMessageType(myMessages);

        // 注册自定义消息展示模版(adapter)
        RongConfigCenter.conversationConfig().addMessageProvider(new CustomMessageProvider());

        // 连接融云服务器
        connectRongCloud();

    }


    /**
     * 初始化融云
     */
    private void initRongCloud() {

        RongIM.init(this, BuildConfig.RONG_CLOUD_APP_KEY, true);

        // 注册自定义的会话列表(此方法在应用生命周期内、主进程中注册一次即可。)
        RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationListActivity, MessageCenterActivity.class);

        // 注册自定义的会话 Activity( 生命周期内、主进程中调用一次即可。)
        RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationActivity, ConversationActivity.class);

    }


    /**
     * 设置融云用户信息提供者
     */
    private void setUserInfoProvider() {

        RongUserInfoManager.getInstance().setUserInfoProvider(userId -> {
            refreshRongUserInfoCache(userId);
            return null;
        }, true);

    }

    /**
     * 刷新融云用户信息
     *
     * @param rongCloudUserId
     */
    public void refreshRongUserInfoCache(String rongCloudUserId) {
        if (User.isLogon()) {
            User appUser = User.getInstance();
            UserRongyunData rongData = appUser.rongData;

            UserInfo userInfo = new UserInfo(rongCloudUserId, rongData.name, Uri.parse(rongData.avatar));
            RongUserInfoManager.getInstance().refreshUserInfoCache(userInfo);
        }
    }

    /**
     * 连接融云服务器
     */
    public void connectRongCloud() {


        if (!User.isLogon()) {
            LogUtils.d("融云", "连接融云服务器，用户未登录");
            return;
        }

        UserRongyunData rongData = User.getInstance().rongData;
        String rongToken = rongData.token;
        if (TextUtils.isEmpty(rongToken)) {
            LogUtils.d("融云", "连接融云服务器，用户已登录，融云token为空");
            return;
        }
        RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {

            /**
             * 连接成功的回调，返回当前连接的用户 ID。
             * @param userId
             */
            @Override
            public void onSuccess(String userId) {
                // 登录成功
                LogUtils.d("融云", "连接融云服务器成功，融云token == " + rongToken + "  当前连接的用户的融云id == " + "userId");
            }

            /**
             * 连接失败并返回对应的连接错误码，开发者需要参考连接相关错误码进行不同业务处理。
             * https://doc.rongcloud.cn/im/Android/5.X/ui/connect/connect#connectstatus
             * @param e
             */
            @Override
            public void onError(RongIMClient.ConnectionErrorCode e) {

            }

            /**
             * 本地数据库打开状态回调。当回调 DATABASE_OPEN_SUCCESS 时，说明本地数据库打开，此时可以拉取本地历史会话及消息，适用于离线登录场景。
             * @param code
             */
            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {

            }
        });


        // 连接状态监听
        RongIM.setConnectionStatusListener(status -> {
            LogUtils.d("融云", "链接状态 status == " + status.name());

        });
    }


    /**
     * 友盟预初始化
     * https://developer.umeng.com/docs/119267/detail/118588
     */
    private void handleUmeng() {
        // 预注册
        // log
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.preInit(this, BuildConfig.UMENG_APP_KEY, BusinessUtils.INSTANCE.getUmengAppChannel());

        // 用户同意协议后的真正注册
        boolean isAgree = CacheUtil.isAgreeUserAgreement();
        if (isAgree) {
            UMConfigure.init(this, BuildConfig.UMENG_APP_KEY, BusinessUtils.INSTANCE.getUmengAppChannel(), UMConfigure.DEVICE_TYPE_PHONE, "");
        }

        // 崩溃回调，拼接自定义字段
        UMCrash.registerUMCrashCallback(() ->
                BuildConfig.FLAVOR + BuildConfig.VERSION_CODE + BuildConfig.VERSION_NAME
        );

        // 自动采集选择
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

    }


    @Override
    public void onTerminate() {

        MobclickAgent.onKillProcess(this);
        super.onTerminate();
    }
}
