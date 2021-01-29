package com.jcs.where.features.account.login;

import com.jcs.where.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
public interface LoginView extends BaseMvpView {
    void LoginSuccess();

    /**
     * 引导注册
     * @param account
     * @param verifyCode
     */
    void guideRegister(String account, String verifyCode);
}
