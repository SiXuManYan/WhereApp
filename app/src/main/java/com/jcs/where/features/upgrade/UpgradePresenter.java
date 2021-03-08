package com.jcs.where.features.upgrade;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/3/8 13:44.
 */
public class UpgradePresenter extends BaseMvpPresenter {

    private UpgradeView view ;

    public UpgradePresenter(UpgradeView baseMvpView) {
        super(baseMvpView);
        view = baseMvpView;
    }
}
