package com.jcs.where.features.message.conversation;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/2/20 15:28.
 */
public class BusinessConversationPresenter extends BaseMvpPresenter {

    private final BusinessConversationView mView;

    public BusinessConversationPresenter(BusinessConversationView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }

    /**
     * 获取聊天列表
     *
     * @param page
     */
    public void getConversationList(int page) {

    }
}
