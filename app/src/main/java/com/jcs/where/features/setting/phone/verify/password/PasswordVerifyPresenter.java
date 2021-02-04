package com.jcs.where.features.setting.phone.verify.password;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/4 15:08.
 */
public class PasswordVerifyPresenter extends BaseMvpPresenter {

    private PasswordVerifyView mView;

    public PasswordVerifyPresenter(PasswordVerifyView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;


    }
}
