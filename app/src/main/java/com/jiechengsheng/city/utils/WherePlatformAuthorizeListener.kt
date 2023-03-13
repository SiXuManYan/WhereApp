package com.jiechengsheng.city.utils

import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformDb

/**
 * Created by Wangsw  2022/8/12 17:05.
 * 授权回调，只处理成功情况
 */
interface WherePlatformAuthorizeListener {

    /**
     * 授权成功
     *
     * @param db 平台用户信息
     */
    fun onComplete(db: PlatformDb)
    fun onError(platform: Platform, i: Int) {

    }

    fun onCancel(platform: Platform, i: Int) {

    }


}