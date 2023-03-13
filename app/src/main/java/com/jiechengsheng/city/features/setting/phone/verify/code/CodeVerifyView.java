package com.jiechengsheng.city.features.setting.phone.verify.code;

import com.jiechengsheng.city.api.network.BaseMvpView;

/**
 * Created by Wangsw  2021/2/4 15:11.
 */
public interface CodeVerifyView extends BaseMvpView {
    void modifyPhoneSuccess();

    void verified();
}
