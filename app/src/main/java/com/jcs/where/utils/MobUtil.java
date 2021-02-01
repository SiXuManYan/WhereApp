package com.jcs.where.utils;

import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;

/**
 * Created by Wangsw  2021/2/1 14:10.
 *
 * @see <a href="https://www.mob.com/wiki/detailed/?wiki=ShareSDK_Others_Share_Authorize&id=undefined">ShareSdk平台分享及授权</a>
 * @see <a href="https://www.mob.com/wiki/detailed/?wiki=ShareSDK_Others_Share_Special_Configuration&id=undefined">ShareSdk facebook 分享单独配置</a>
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


    public static void authorize(Platform plat, WherePlatformAuthorizeListener listener) {
        if (plat == null) {
            return;
        }
        if (!plat.isClientValid()) {
            ToastUtils.showShort("客户端不存在");
            return;
        }
        //判断指定平台是否已经存在授权状态
        if (plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (userId != null) {
                listener.onComplete(plat.getDb());
                return;
            }
        }
        plat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort("授权失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.showShort("授权取消");
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

}
