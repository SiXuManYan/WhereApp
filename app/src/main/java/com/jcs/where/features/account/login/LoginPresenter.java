package com.jcs.where.features.account.login;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
public class LoginPresenter extends BaseMvpPresenter {

    private final LoginView mView;


    public LoginPresenter(LoginView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
