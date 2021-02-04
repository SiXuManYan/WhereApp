package com.jcs.where.features.setting.phone.verify.code;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/4 15:11.
 */
public class CodeVerifyPresenter extends BaseMvpPresenter {

    private CodeVerifyView mView;

    public CodeVerifyPresenter(CodeVerifyView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
