package com.jcs.where.features.account.password;

import com.jcs.where.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/1/30 10:33.
 */
public interface PasswordResetView extends BaseMvpView {

    /**
     * 密码重置成功
     */
    void passwordResetSuccess();
}
