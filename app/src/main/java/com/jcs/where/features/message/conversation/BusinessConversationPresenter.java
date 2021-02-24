package com.jcs.where.features.message.conversation;

import android.net.Uri;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.message.RongCloudUserResponse;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

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
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        mView.getListError(errorCode);
                    }

                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        boolean isLastPage = true;
                        if (conversations != null) {
                            isLastPage = conversations.size() < 10;

                            // 校验target 信息是否为空
                            for (Conversation conversation : conversations) {

                                String targetId = conversation.getTargetId();
                                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                                if (userInfo == null) {
                                    getRongCloudUserInfo(targetId);
                                }
                            }
                        }


                        mView.bindList(conversations, isLastPage);
                    }


                }, timeStamp, count, Conversation.ConversationType.PRIVATE
        );
    }

    private void getRongCloudUserInfo(String targetId) {

        requestApi(mRetrofit.getRongCloudUserInfo(targetId), new BaseMvpObserver<RongCloudUserResponse>(mView) {
            @Override
            protected void onSuccess(RongCloudUserResponse response) {

                // 刷新融云用户信息缓存
                UserInfo userInfo = new UserInfo(targetId, response.name, Uri.parse(response.avatar));
                RongIM.getInstance().refreshUserInfoCache(userInfo);
            }
        });

    }

}
