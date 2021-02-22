package com.jcs.where.features.message.notice;

import com.google.gson.JsonElement;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.message.MessageStatusRequest;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.message.SystemMessageResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/2/20 15:40.
 */
public class SystemNoticePresenter extends BaseMvpPresenter {

    private final SystemNoticeView mView;

    public SystemNoticePresenter(SystemNoticeView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }

    public void getMessageList(int page) {

        requestApi(mRetrofit.getSystemMessage(page), new BaseMvpObserver<PageResponse<SystemMessageResponse>>(mView) {
            @Override
            protected void onSuccess(PageResponse<SystemMessageResponse> response) {
                boolean isLastPage = response.getLastPage() == page;
                List<SystemMessageResponse> data = response.getData();
                mView.bindList(data, isLastPage);
            }
        });
    }

    public void setMessageRead(String id) {
        MessageStatusRequest request = new MessageStatusRequest();
        request.id = id;
        requestApi(mRetrofit.setMessageRead(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {

            }
        });
    }
}
