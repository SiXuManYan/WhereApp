package com.jcs.where.features.account.login;

import com.jcs.where.api.network.BaseMvpView;

import cn.sharesdk.framework.PlatformDb;

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
public interface LoginView extends BaseMvpView {

    /**
     * 登录成功（验证码、密码、第三方登录）
     */
    void LoginSuccess();

    /**
     * 引导注册
     *
     * @param account
     * @param verifyCode
     */
    void guideRegister(String account, String verifyCode);

    /**
     * 第三方登录的新用户，
     * 引导至绑定手机号页面
     * @param platformData
     * @param loginType
     */
    void guideToAccountBind(PlatformDb platformData, int loginType );

    void authorizeCancel();

    void authorizeError();
}
