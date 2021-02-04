package com.jcs.where.features.setting.information;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/4 12:00.
 */
public class ModifyInfoPresenter extends BaseMvpPresenter {

    private ModifyInfoView mView ;

    public ModifyInfoPresenter(ModifyInfoView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
