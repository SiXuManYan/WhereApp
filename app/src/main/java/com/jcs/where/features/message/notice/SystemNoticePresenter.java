package com.jcs.where.features.message.notice;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/20 15:40.
 */
public class SystemNoticePresenter extends BaseMvpPresenter {

    private final SystemNoticeView mView;

    public SystemNoticePresenter(SystemNoticeView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }
}
