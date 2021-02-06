package com.jcs.where.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Wangsw  2021/2/1 14:10.
 * mob 统一处理三方登录以及分享
 * sharesdk 文档
 *
 * @see <a href="https://www.mob.com/wiki/detailed/?wiki=ShareSDK_Android_title_zhuce&id=14">第三方平台注册流程</a>
 * @see <a href="https://www.mob.com/wiki/detailed/?wiki=ShareSDK_Others_Share_Authorize&id=undefined">ShareSdk平台分享及授权</a>
 * @see <a href="https://www.mob.com/wiki/detailed/?wiki=ShareSDK_Others_Share_Special_Configuration&id=undefined">ShareSdk facebook 分享单独配置</a>
 * <p>
 * Facebook
 * @see <a href="https://www.jianshu.com/p/f6f66d2a4297">生成Facebook Key Hash</a>
 * @see <a href="https://link.jianshu.com/?t=http://downloads.sourceforge.net/gnuwin32/openssl-0.9.8h-1-bin.zip">OpenSSL 工具下载</a>
 * <p>
 * Google
 * @see <a href="https://developers.google.com/identity/sign-in/android/start-integrating">配置</a>
 */
public class MobUtil {


    /**
     * 授权回调，只处理成功情况
     */
    public interface WherePlatformAuthorizeListener {

        /**
         * 授权成功
         *
         * @param db 平台用户信息
         */
        void onComplete(PlatformDb db);
    }


    /**
     * 分享回调，只处理成功情况
     */
    public interface WherePlatformShareListener {


        /**
         * 分享成功
         */
        void onShareComplete(Platform platform, int i, HashMap<String, Object> hashMap);
    }

    public static void authorize(Platform plat, WherePlatformAuthorizeListener listener) {
        if (plat == null) {
            return;
        }
        if (!plat.isClientValid()) {
            ToastUtils.showShort(R.string.is_client_valid);
            return;
        }

        if (plat.isAuthValid()) {
            // 已授权过，重新授权
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort(R.string.authorization_failed);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort(R.string.authorization_cancel);
            }

            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onComplete(platform.getDb());

            }
        });

        plat.SSOSetting(false);  // true不使用SSO授权，false使用SSO授权
        // ShareSDK.setActivity(this);//抖音登录适配安卓9.0
        plat.showUser(null); // 获取用户资料
    }

    /**
     * 分享文本
     *
     * @param platformName 平台名称 Facebook.NAME GooglePlus.NAME
     */
    public static void shareText(
            String platformName,
            String title,
            String text,
            WherePlatformShareListener listener) {

        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat == null) {
            return;
        }
        if (!plat.isClientValid()) {
            ToastUtils.showShort(R.string.is_client_valid);
            return;
        }
        ShareSDK.deleteCache();
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        shareParams.setShareType(Platform.SHARE_TEXT);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onShareComplete(platform, i, hashMap);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort(R.string.share_failed);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort(R.string.share_cancel);
            }
        });
        plat.share(shareParams);
    }

    /**
     * 分享网页
     *
     * @param platformName 平台名称 Facebook.NAME GooglePlus.NAME
     */
    public static void shareWebPager(
            String platformName,
            String title,
            String text,
            String imageUrl,
            String webUrl,
            WherePlatformShareListener listener) {
        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat == null) {
            return;
        }
        if (!plat.isClientValid()) {
            ToastUtils.showShort(R.string.is_client_valid);
            return;
        }

        ShareSDK.deleteCache();
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        shareParams.setText(text);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        shareParams.setUrl(webUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);

        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onShareComplete(platform, i, hashMap);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort(R.string.share_failed);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort(R.string.share_cancel);
            }
        });
        plat.share(shareParams);
    }


    /**
     * 分享图片
     *
     * @param platformName 平台名称 Facebook.NAME GooglePlus.NAME
     */
    public static void shareImage(
            String platformName,
            String title,
            String imageUrl,
            WherePlatformShareListener listener) {
        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat == null) {
            return;
        }
        if (!plat.isClientValid()) {
            ToastUtils.showShort(R.string.is_client_valid);
            return;
        }

        ShareSDK.deleteCache();
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(title);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareParams.setImageUrl(imageUrl);
        }
        shareParams.setShareType(Platform.SHARE_IMAGE);

        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onShareComplete(platform, i, hashMap);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort(R.string.share_failed);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort(R.string.share_cancel);
            }
        });
        plat.share(shareParams);
    }


}
