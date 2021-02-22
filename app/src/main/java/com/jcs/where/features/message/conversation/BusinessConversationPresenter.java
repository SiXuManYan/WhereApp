package com.jcs.where.features.message.conversation;

import com.jcs.where.api.network.BaseMvpPresenter;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

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
     */
    public void getConversationList(int page, Long timeStamp) {

        int count = 10;
        RongIMClient.getInstance().getConversationListByPage(
                new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        boolean isLastPage = true;

                        if (conversations != null) {
                            isLastPage = conversations.size() < 10;
                        }
                        mView.bindList(conversations, isLastPage);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        mView.getListError(errorCode);
                    }
                }, timeStamp, count, Conversation.ConversationType.PRIVATE
        );
    }
}
