package com.jcs.where.features.message.conversation;

import com.jcs.where.api.network.BaseMvpView;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Wangsw  2021/2/20 15:28.
 */
public interface BusinessConversationView extends BaseMvpView {
    void bindList(List<Conversation> conversations, boolean isLastPage);

    void getListError(RongIMClient.ErrorCode errorCode);
}
