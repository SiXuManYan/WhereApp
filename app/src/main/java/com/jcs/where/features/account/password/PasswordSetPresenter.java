package com.jcs.where.features.account.password;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/1/29 16:51.
 */
public class PasswordSetPresenter extends BaseMvpPresenter {

    private final PasswordSetView mView;

    public PasswordSetPresenter(PasswordSetView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;

    }
}
