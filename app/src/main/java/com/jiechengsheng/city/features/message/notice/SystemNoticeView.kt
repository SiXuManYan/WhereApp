package com.jiechengsheng.city.features.message.notice

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.message.SystemMessageResponse

/**
 * Created by Wangsw  2021/2/20 15:39.
 */
interface SystemNoticeView : BaseMvpView {
    fun bindList(data: List<SystemMessageResponse>, isLastPage: Boolean){}
    fun bindUnreadMessageCount(systemCount: Int){}
}