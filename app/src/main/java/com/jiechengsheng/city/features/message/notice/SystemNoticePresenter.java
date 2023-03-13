package com.jiechengsheng.city.features.message.notice;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.request.message.MessageStatusRequest;
import com.jiechengsheng.city.api.response.PageResponse;
import com.jiechengsheng.city.api.response.message.SystemMessageResponse;

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

        requestApi(mRetrofit.getSystemMessage(page), new BaseMvpObserver<PageResponse<SystemMessageResponse>>(mView, page) {
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

            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
            }
        });
    }
}
