package com.jiechengsheng.city.features.setting.phone.verify.password;

import com.jiechengsheng.city.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/2/4 15:08.
 */
public interface PasswordVerifyView extends BaseMvpView {


    /**
     * 密码校验通过
     */
    void passwordCheckPass();
}
