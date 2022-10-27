package com.jcs.where.utils;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
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
                listener.onError(platform , i );
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort(R.string.authorization_cancel);
                listener.onCancel(platform , i );
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


    public static void shareFacebookWebPage(String webUrl, FragmentActivity activity) {
        Platform plat = ShareSDK.getPlatform(Facebook.NAME);
        if (!plat.isClientValid()) {
//            ToastUtils.showShort(R.string.is_client_valid_facebook);
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.prompt)
                    .setMessage(R.string.is_client_valid_facebook)
                    .setPositiveButton(R.string.install, (dialogInterface, i) -> {
                        Uri uri = Uri.parse("market://details?id=com.facebook.katana");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .create().show();
            return;
        }
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setUrl(webUrl);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setQuote(StringUtils.getString(R.string.app_name));
        // shareParams.setHashtag("..."); 编辑框内可以加上默认文字
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.showShort(R.string.share_success);
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
