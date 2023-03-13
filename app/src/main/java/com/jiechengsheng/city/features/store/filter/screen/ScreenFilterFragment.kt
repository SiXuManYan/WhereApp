package com.jiechengsheng.city.features.store.filter.screen

import android.view.View
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_filter_more.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/6/10 14:41.
 *  商城搜索，更多
 */
class ScreenFilterFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_filter_more

    private lateinit var moreFilter: ScreenMoreFilter

    override fun initView(view: View) = Unit

    override fun initData() {
        moreFilter = ScreenMoreFilter()
    }

    override fun bindListener() {

        business_service_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.self_rb -> moreFilter.delivery_type = 1
                R.id.express_rb -> moreFilter.delivery_type = 2
            }
        }

        sort_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.distance_rb -> moreFilter.sort_type = 1
                R.id.favourable_rb -> moreFilter.sort_type = 2
            }
        }

        reset_tv.setOnClickListener {
            moreFilter.delivery_type = null
            moreFilter.sort_type = null
        }

        confirm_tv.setOnClickListener {
            EventBus.getDefault().post(BaseEvent(moreFilter))
        }

    }


    class ScreenMoreFilter {

        /**
         * 	商家服务/配送方式（1:自提，2:商家配送）
         */
        var delivery_type: Int? = null

        /**
         * 排序方式(1:距离优先，2:好评优先）
         */
        var sort_type: Int? = null

    }

}