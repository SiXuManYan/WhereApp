package com.jcs.where.features.setting.password;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/4 14:33.
 */
public class ModifyPasswordPresenter extends BaseMvpPresenter {

    private ModifyPasswordView mView;

    public ModifyPasswordPresenter(ModifyPasswordView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
