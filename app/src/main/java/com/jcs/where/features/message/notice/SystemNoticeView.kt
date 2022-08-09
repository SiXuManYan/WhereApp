package com.jcs.where.features.message.notice;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.message.SystemMessageResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/2/20 15:39.
 */
public interface SystemNoticeView extends BaseMvpView {

    void bindList(List<SystemMessageResponse> data, boolean isLastPage);
}
