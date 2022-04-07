package com.jcs.where.features.setting.phone;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/4 14:48.
 */
public class ModifyPhonePresenter extends BaseMvpPresenter {

    private ModifyPhoneView mView;

    public ModifyPhonePresenter(ModifyPhoneView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
