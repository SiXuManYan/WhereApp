package com.jcs.where.features.message.notice

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.message.SystemMessageResponse

/**
 * Created by Wangsw  2021/2/20 15:39.
 */
interface SystemNoticeView : BaseMvpView {
    fun bindList(data: List<SystemMessageResponse>, isLastPage: Boolean)
}