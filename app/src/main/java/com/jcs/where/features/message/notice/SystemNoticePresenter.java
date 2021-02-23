package com.jcs.where.features.message.notice;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.message.MessageStatusRequest;
import com.jcs.where.api.response.PageResponse;
import com.jcs.where.api.response.message.SystemMessageResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    public void setMessageRead(@NotNull ArrayList<String> id) {


        if (id.isEmpty()) {
            return;
        }

        //  {"id":"["66"]"}
        Gson gson = new Gson();
        String s = gson.toJson(id);

        MessageStatusRequest request = new MessageStatusRequest();
        request.id = s;


        requestApi(mRetrofit.setMessageRead(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {
                ToastUtils.showShort("sadasd");
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                ToastUtils.showShort("111111111111111111");
            }
        });
    }
}
